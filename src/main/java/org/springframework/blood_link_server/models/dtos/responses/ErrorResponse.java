package org.springframework.blood_link_server.models.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ErrorResponse {
    private String error;
    private long timestamp;

    public ErrorResponse(String error){
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
}
