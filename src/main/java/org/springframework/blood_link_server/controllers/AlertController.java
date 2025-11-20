package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.services.interfaces.AlertService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("api/v1/alert")

public class AlertController {

    private final AlertService alertService;
    private final JwtService jwtService;

    @PostMapping("create-alert/{recipientType}")
    public ResponseEntity<?> createAlert(HttpServletRequest httpRequest, @PathVariable BloodType recipientType){
        try {
            var username = getUsername(httpRequest);

            return ResponseEntity.ok(alertService.createSendAlert(username, recipientType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
