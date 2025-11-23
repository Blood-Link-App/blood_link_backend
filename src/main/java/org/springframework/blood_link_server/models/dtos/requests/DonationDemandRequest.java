package org.springframework.blood_link_server.models.dtos.requests;

import org.springframework.blood_link_server.models.enumerations.RequestStatus;


public record DonationDemandRequest (
        String bankName,
        RequestStatus status
) {}
