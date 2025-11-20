package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.Alert;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.repositories.AlertRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.services.interfaces.AlertService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor

public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final BloodBankRepository bankRepository;



    /**
     * @param request is the parameter necessary to implement the method createSendAlert
     * @return an Alert
     */
    @Override
    public Alert createSendAlert(BloodRequest request) {

        if(!verifyCreate(request)){
            return null;
        }

        Alert alert = Alert.builder()
                .status(AlertStatus.PENDING)
                .compatibleTypes(BloodType.getCompatibleDonors(request.getRecipientType()))
                .quantity(request.getQuantityNeeded())
                .build();

        return alertRepository.saveAndFlush(alert);
    }

    /**
     * @return an alert
     */
    @Override
    public Alert createSendAlert(String username, BloodType bloodTypeNeeded) {
        BloodBank bank = bankRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("blood bank not found"));
        Alert alert = Alert.builder()
                .status(AlertStatus.PENDING)
                .compatibleTypes(BloodType.getCompatibleDonors(bloodTypeNeeded))
                .bloodRequest(BloodRequest.builder().quantityNeeded(0L).bloodBank(bank).build())
                .quantity(0L)
                .build();

        return alertRepository.save(alert);
    }

    /**
     * @return the list of the Alert
     */

    @Override
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
        //return List.of();
    }

    public static boolean verifyCreate(BloodRequest request){
        return request.getRecipientType() != null
                && request.getQuantityNeeded() > 0
                && request.getGroupRequestId() != null
                && request.getDoctor().getId() != null
                && request.getStatus() != RequestStatus.COMPLETED;
    }

}
