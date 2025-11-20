package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.Alert;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;

import java.util.List;

public interface AlertService {

    Alert createSendAlert(BloodRequest request);

    Alert createSendAlert(String username, BloodType bloodTypeNeeded);

    List <Alert> getAllAlerts();
}
