package org.springframework.blood_link_server.models.dtos.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationResponse {
    String token;
    String email;
    String role;
}
