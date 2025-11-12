package org.springframework.blood_link_server.models.dtos.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor

public class BloodDemandRequest {

    private BloodType recipientType;
    private Long quantityNeeded;
    private RequestStatus status;
    private List<String > bankNames;

}
