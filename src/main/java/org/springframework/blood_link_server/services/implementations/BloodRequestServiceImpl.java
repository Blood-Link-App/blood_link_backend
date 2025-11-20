package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.BloodDemandRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.BloodRequestRepository;
import org.springframework.blood_link_server.repositories.DoctorRepository;
import org.springframework.blood_link_server.repositories.StockByTypeRepository;
import org.springframework.blood_link_server.services.interfaces.AlertService;
import org.springframework.blood_link_server.services.interfaces.BloodRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.blood_link_server.models.enumerations.BloodType.getCompatibleDonors;

@Service
@RequiredArgsConstructor
public class BloodRequestServiceImpl implements BloodRequestService {

    final BloodBankRepository bloodBankRepository;
    final BloodRequestRepository bloodRequestRepository;
    final DoctorRepository doctorRepository;
    final StockByTypeRepository typeRepository;
    private final AlertService alertService;

    /**
     * @param username is the email of the doctor who made the request
     * @param request is the pattern of all the blood requests
     * @return a list of blood requests
     */


    @Override
    public List<BloodRequest> sendRequests(String username, BloodDemandRequest request){


        if(verifyCreate(request)){
            return null;
        }

        Doctor doctor = doctorRepository.findDoctorByEmail(username).orElseThrow(() -> new RuntimeException("No doctor found"));



        final UUID groupRequestId = UUID.randomUUID();
        List<BloodRequest> bloodRequests = request.getBankNames().stream().map(name ->
                BloodRequest.builder()
                        .bloodBank(bloodBankRepository.findByBloodBankName(name).orElseThrow(() -> new RuntimeException("Blood bank not found " + name)))
                        .quantityNeeded(request.getQuantityNeeded())
                        .recipientType(request.getRecipientType())
                        .doctor(doctor)
                        .groupRequestId(groupRequestId)
                        .status(RequestStatus.PENDING)
                        .build()
                ).collect(Collectors.toList());


        return bloodRequestRepository.saveAllAndFlush(bloodRequests);
    }



    /**
     * @param status is the parameter needed to find pending blood requests
     * @return a list of pending blood requests
     */

    //@PreAuthorize("hasRole(T(org.springframework.blood_link_server.models.enumerations.UserRole).BLOODBANK.name())")
    @Override
    public List<BloodRequest> getPendingBloodRequests(RequestStatus status) {
        return bloodRequestRepository.findBloodRequestsByStatus(status) /*List.of()*/;
    }

    /**
     * @param status pending
     * @return a list of pending blood requests
     */
    @Override
    public List<BloodRequest> getPendingBloodRequestsByIdAndStatus(UUID bankId, RequestStatus status) {
        if (bloodBankRepository.findById(bankId).isEmpty()){
            return null ;
        }
        return bloodRequestRepository.findByIdAndStatus(bankId, status)
                /*List.of()*/;
    }


    // Process the blood request after receiving it

    /**
     * @param username  is the parameter needed to find pending blood requests
     * @param requestId a list of pending blood requests
     * @return either ...
     */


    @Override
    public BloodRequest processingBloodRequest(String username, UUID requestId) {

        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException("Blood bank not found"));

        BloodBank bank = request.getBloodBank();
        BloodBankStock bankStock = bank.getStock();

        List<BloodType> compatibleTypes = getCompatibleDonors(request.getRecipientType());

        Optional<StockByType> availableStocks = bankStock.getStockByTypeList().stream()
                .filter(s -> compatibleTypes.contains(s.getBloodType()) && s.getQuantity() > request.getQuantityNeeded())
                .findFirst();

        BloodRequest response;

        if(availableStocks.isPresent()){

            StockByType availableStockType = availableStocks.get();
            availableStockType.setQuantity(availableStockType.getQuantity() - request.getQuantityNeeded());
            typeRepository.save(availableStockType);

            request.setStatus(RequestStatus.COMPLETED);
            response = bloodRequestRepository.save(request);

            List<BloodRequest> otherRequests = bloodRequestRepository.findByGroupRequestId(request.getGroupRequestId()).stream()
                    .filter(r -> r.getId() != request.getId())
                    .toList();

            if (!otherRequests.isEmpty())
                for (BloodRequest otherRequest : otherRequests) {
                    otherRequest.setStatus(RequestStatus.CANCELED);
                }

            bloodRequestRepository.saveAll(otherRequests);
            return response;
        }  else {
            request.setAlert(alertService.createSendAlert(request));
            request.setStatus(RequestStatus.PROCESSING);
            response = bloodRequestRepository.save(request);
        }

        return response;
    }


// This method is used to verify if each attribute iof the BloodDemandRequest aren't blank
    private boolean verifyCreate(BloodDemandRequest request){
        return request.getStatus() == null ||
                request.getBankNames().isEmpty() ||
                request.getQuantityNeeded() == null ||
                request.getRecipientType() == null;
    }
}
