package org.springframework.blood_link_server.models.dtos.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;

@Getter
@Setter
@RequiredArgsConstructor

public class AlertRequest {
    private AlertStatus alertStatus;
    private BloodRequest request;
}
