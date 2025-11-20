package org.springframework.blood_link_server.models.dtos.requests;

import org.springframework.blood_link_server.models.enumerations.Gender;

import java.time.LocalDate;

public record InfosRequest(
        LocalDate birthdate,
        Gender gender,
        float weight,
        String emergencyContact
)
{}
