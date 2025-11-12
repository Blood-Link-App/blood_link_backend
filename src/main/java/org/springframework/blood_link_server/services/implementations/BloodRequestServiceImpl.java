package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.Alert;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.dtos.requests.BloodDemandRequest;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.BloodRequestRepository;
import org.springframework.blood_link_server.repositories.DoctorRepository;
import org.springframework.blood_link_server.services.interfaces.AlertService;
import org.springframework.blood_link_server.services.interfaces.BloodRequestService;
import org.springframework.blood_link_server.services.interfaces.StockByTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodRequestServiceImpl implements BloodRequestService {

    //final UserRepository userRepository;
    final BloodBankRepository bloodBankRepository;
    final BloodRequestRepository bloodRequestRepository;
    final DoctorRepository doctorRepository;
    final StockByTypeService stockByTypeService;
    private final AlertService alertService;

    /**
     * @param username is the email of the doctor who made the request
     * @param request is the pattern of all the blood requests
     * @return a list of blood requests
     */
    @Override
    public List<BloodRequest> sendRequests(String username, BloodDemandRequest request){

       /* username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
*/
        if(verifyCreate(request)){
            return null;
        }

        Doctor doctor = doctorRepository.findDoctorByEmail(username).orElseThrow(() -> new RuntimeException("No doctor found"));
/*        Doctor doctor = (Doctor) userRepository.findByEmail(username).orElse(null);
        if (doctor == null)
            return List.of(null);*/

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




/*
       boolean test = bloodRequestRepository.findAll(bloodRequests);
        if(test) {
            return bloodRequestRepository.saveAllAndFlush(bloodRequests);
        }*/
        return bloodRequestRepository.saveAllAndFlush(bloodRequests);
        //return null;
    }

    /**
     * @param status is the parameter needed to find pending blood requests
     * @return a list of pending blood requests
     */
    @Override
    public List<BloodRequest> getPendingBloodRequests(RequestStatus status) {
        return bloodRequestRepository.findBloodRequestsByStatus(status) /*List.of()*/;
    }

    /**
     * @param requestId is the parameter needed to find a blood request and process it
     */
    @Override
    public void processingBloodRequest(String username, UUID requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Blood request not found"));

        StockByTypeRequest stockByTypeRequest = StockByTypeRequest.builder()
                .bloodType(request.getRecipientType())
                .quantity(Math.toIntExact(request.getQuantityNeeded()))
                .build();



        boolean available = stockByTypeService.checkAvailability(stockByTypeRequest) && stockByTypeService.checkAvailabilityWithCompatibility(stockByTypeRequest);
        if (available) {
            request.setStatus(RequestStatus.COMPLETED);
            bloodRequestRepository.save(request);
            return;
        }

        Alert alert = alertService.createSendAlert(request, BloodType.getCompatibleDonors(request.getRecipientType()));
        request.setAlert(alert);

        request.setStatus(RequestStatus.PROCESSING);
        bloodRequestRepository.save(request);

    }




/*
    @Override
    public Set<BloodRequest> sendRequests1(String username, BloodDemandRequest request) {

        if(verifyCreate(request)){
            return null;
        }
        Doctor doctor = doctorRepository.findDoctorByEmail(username).orElseThrow(() -> new RuntimeException("No doctor found"));

        Set <BloodRequest> bloodRequests1 = request.getBankNames().stream().map(name -> {
                    BloodBank bloodBank = bloodBankRepository.findByBloodBankName(name).orElseThrow(() -> new RuntimeException("Blood bank not found " + name));
                    return
                            BloodRequest.builder()
                                    .bloodBank(bloodBank)
                                    .quantityNeeded(request.getQuantityNeeded())
                                    .recipientType(request.getRecipientType())
                                    .doctor(doctor)
                                    .status(RequestStatus.PENDING)
                                    .build();
                })
                .collect(Collectors.toSet());

        return (Set<BloodRequest>) bloodRequestRepository.saveAllAndFlush(bloodRequests1);
    }*/

    private boolean verifyCreate(BloodDemandRequest request){
        return request.getStatus() == null ||
                request.getBankNames().isEmpty() ||
                request.getQuantityNeeded() == null ||
                request.getRecipientType() == null;
    }
}
