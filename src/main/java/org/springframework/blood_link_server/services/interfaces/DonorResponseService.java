package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.DonorResponse;
import org.springframework.blood_link_server.models.dtos.requests.ResponseRequest;

import java.util.UUID;


public interface DonorResponseService {
    DonorResponse createResponseToAnAlertNotification (UUID notifID, String username, ResponseRequest responseDTo);

}

