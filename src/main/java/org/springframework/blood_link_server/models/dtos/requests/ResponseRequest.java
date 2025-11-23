package org.springframework.blood_link_server.models.dtos.requests;

import org.springframework.blood_link_server.models.enumerations.AlertResponse;

public record ResponseRequest(
        AlertResponse response
){}
