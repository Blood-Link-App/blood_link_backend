package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.Alert;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.repositories.AlertRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.services.interfaces.AlertService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AlertServiceImpl implements AlertService {

    private final BloodBankRepository bankRepository;
    private final AlertRepository alertRepository;


    /**
     * @param request          is the parameter necessary to implement the method createSendAlert
     * @param compatibleDonors ghj[pjh
     * @return an Alert
     */
    @Override
    public Alert createSendAlert(BloodRequest request, List<BloodType> compatibleDonors) {

        Alert alert = Alert.builder()
                .status(AlertStatus.PENDING)
                .bloodBank(request.getBloodBank())
                .compatibleTypes(BloodType.getCompatibleDonors(request.getRecipientType()))
                .quantity(Math.toIntExact(request.getQuantityNeeded()))
                .build();
        alert = alertRepository.save(alert);


        return alert ;
    }

    /**
     * @return the list of the Alert
     */
    @Override
    public List<Alert> getAllAlerts() {
        return List.of();
    }
}
