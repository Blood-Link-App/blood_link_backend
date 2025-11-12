package org.springframework.blood_link_server.models.dtos.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor

public class LoginRequest {
    private String email;
    private String password;
}
