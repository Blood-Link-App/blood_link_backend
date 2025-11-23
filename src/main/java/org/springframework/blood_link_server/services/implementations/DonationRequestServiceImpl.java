package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.*;
import org.springframework.blood_link_server.models.dtos.requests.DonationDemandRequest;
import org.springframework.blood_link_server.models.enumerations.AlertResponse;
import org.springframework.blood_link_server.models.enumerations.DonationStatus;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.medicalProfile.MedicalProfile;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.repositories.*;
import org.springframework.blood_link_server.services.interfaces.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DonationRequestServiceImpl implements DonationRequestService {

    private final NotificationRepository notifRepository;
    private final DonorRepository donorRepository;
    private final DonationRequestRepository requestRepository;
    private final BloodBankRepository bankRepository;
    private final DonationService donationService;
    private final BankStockRepository stockRepository;
    private final StockByTypeRepository typeRepository;
    private final DonationRepository donationRepository;
    private final BankStockService stockService;

    /**
     * @param username of the connected donor
     * @param demandRequest is the parameter needed to make a donation request
     * @param notifId id of the notification demand the donor wants to make a donation for
     * @return a donation request
     */

    @Override
    public DonationRequest createDonationRequest(String username, UUID notifId, DonationDemandRequest demandRequest) {

        Notification notif = notifRepository.findById(notifId).orElse(null);

        Donor donor = donorRepository.findByEmail(username).orElse(null);

        BloodBank bank = bankRepository.findByBloodBankName(demandRequest.bankName()).orElse(null);

        if(notif == null){
            throw new EntityNotFoundException("Notification no found");
        }

        if(donor == null){
            throw new EntityNotFoundException("Donor no found");
        }

        if(bank == null){
            throw new EntityNotFoundException("Bank not found");
        }

        if (!notif.getDonor().getId().equals(donor.getId())){
            throw new AccessDeniedException("User unauthorized");
        }

        if (!donor.isAffiliated(bank)){
            throw new AccessDeniedException("User unauthorized");
        }

        DonationRequest request = DonationRequest.builder()
                .bloodBank(bank)
                .status(RequestStatus.PENDING)
                .donor(donor)
                .build();

        return requestRepository.save(request);
    }



    /**
     * @param username of the concerned blood bank
     * @param donationRequestId of the donation request to process
     * @return Donation or else
     */


    @Override
    public Donation processDonationRequest(String username, UUID donationRequestId) {

        DonationRequest donationRequest = requestRepository.findById(donationRequestId).orElse(null);

        BloodBank bank = bankRepository.findByEmail(username).orElse(null);

        if(donationRequest == null){
            throw new EntityNotFoundException("Donation request not found");
        }

        if(bank == null){
            throw new EntityNotFoundException("Blood bank not found");
        }

        if (!bank.getId().equals(donationRequest.getBloodBank().getId())){
            throw new AccessDeniedException("Unauthorized");
        }

        Donor donor = donationRequest.getDonor();

        MedicalProfile profile = donor.getMedicalProfile() ;

        if (profile == null){
            throw new RuntimeException("Medical profile not initialized yet");
        }

        Notification notification = null;

        List<Notification> notifs = donor.getNotifications();

        for (Notification notif: notifs){
            if (notif.getDonorResponse() != null){
                notification = notif;
            }
        }

        if(notification == null){
            throw new RuntimeException("NO responses yet");
        }

        if(!notification.getDonorResponse().getResponse().equals(AlertResponse.APPROVE))
        {
            throw new IllegalArgumentException("The donor response should be approve");
        }
        
        if(!donationService.checkEligibility(username, profile.getId())){
            donationRequest.setStatus(RequestStatus.REJECTED);
            requestRepository.save(donationRequest);
            throw new RuntimeException("The donor does not meet donation's eligibility criteria");
        }


        Donation donation = Donation.builder()
                .bloodType(donor.getBloodType())
                .donationDate(LocalDateTime.now())
                .isValidated(true)
                .status(DonationStatus.PENDING)
                .unitsGave(1L)
                .build();


        BloodBankStock bankStock = bank.getStock();

        StockByType stock = stockService.getStockBYtype(bankStock.getId(),donationRequest.getDonor().getBloodType());

        stock.setQuantity(stock.getQuantity() + donation.getUnitsGave());
//        stock.upgradeQuantity(donation.getUnitsGave());

        bankStock.updateTotalQuantity();

        donationRequest.setDonation(donation);
        donationRequest.setStatus(RequestStatus.COMPLETED);

        typeRepository.save(stock);
        stockRepository.save(bankStock);
        bankRepository.save(bank);
        return requestRepository.save(donationRequest).getDonation();
    }

}
