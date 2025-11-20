package org.springframework.blood_link_server.models.dtos.requests;

public record SignsRequest(

        float hemoglobinLevel,

        float bodyTemperature,

        float pulseRate
)

{}
