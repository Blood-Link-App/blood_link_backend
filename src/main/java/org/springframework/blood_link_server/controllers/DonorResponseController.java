package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonorResponse;
import org.springframework.blood_link_server.models.dtos.requests.ResponseRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.services.interfaces.DonorResponseService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/donor-response")

@RequiredArgsConstructor

public class DonorResponseController {

    private final JwtService jwtService;

    private final DonorResponseService responseService;

    @PostMapping("create-donor-response/{notifID}")
    public ResponseEntity<?> createDonorResponse( HttpServletRequest httpServlet, @PathVariable UUID notifID, @RequestBody ResponseRequest request) {
        try{
            String username = getUsername(httpServlet);

            DonorResponse donorResponse = responseService.createResponseToAnAlertNotification(notifID, username, request);
            return ResponseEntity.ok(donorResponse);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
