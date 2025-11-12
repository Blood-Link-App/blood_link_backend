package org.springframework.blood_link_server.models.enumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertResponse {
    APPROVE(true),
    DECLINE(false);

    final boolean response;

    public static AlertResponse findByResponse(boolean response){
        for (AlertResponse alertResponse: AlertResponse.values()){
            if (alertResponse.response == response) {
                return alertResponse;
            }
        } throw new IllegalArgumentException("Invalid Alert response " + response);
    }

}
