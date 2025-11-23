package org.springframework.blood_link_server.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.Donation;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.dtos.requests.DonationDemandRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.services.interfaces.DonationRequestService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/donation-request")
@RequiredArgsConstructor

public class DonationRequestController {

    private final JwtService jwtService;
    private final DonationRequestService requestService;


    @PostMapping("create-donation-request/{notifId}")
    public ResponseEntity<?> createDonationRequest(HttpServletRequest httpServlet, @PathVariable UUID notifId, @RequestBody DonationDemandRequest demandRequest) {
        try {

            String username = getUsername(httpServlet);
            DonationRequest donationRequest = requestService.createDonationRequest(username, notifId, demandRequest);
            return ResponseEntity.ok(donationRequest);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("process-donation-request/{donationRequestId}")
    public ResponseEntity<?> processDonationRequest(HttpServletRequest httpServlet, @PathVariable UUID donationRequestId ) {
        try {
            String username = getUsername(httpServlet);
            Donation donation = requestService.processDonationRequest(username, donationRequestId);
            return ResponseEntity.ok(donation);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }

}