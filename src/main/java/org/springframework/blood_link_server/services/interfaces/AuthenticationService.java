package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.dtos.requests.LoginRequest;
import org.springframework.blood_link_server.models.dtos.requests.RegisterRequest;
import org.springframework.blood_link_server.models.dtos.responses.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    AuthenticationResponse registerUser (RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
}
