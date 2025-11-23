package org.springframework.blood_link_server.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.services.interfaces.DonorService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/donor")

@RequiredArgsConstructor

public class DonorController {

     final DonorService donorService;
     private final JwtService jwtService;


   @PostMapping("affiliation-donor-blood-bank/{bankId}")
    public ResponseEntity<?> affiliateDonorToBloodBank(HttpServletRequest request, @PathVariable UUID bankId) {
        try {
            final String authHeader = request.getHeader("Authorization");
            var jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);

            donorService.affiliateDonorToBloodBank(username, bankId);
            return ResponseEntity.ok("Successful Affiliation !!");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
            }
        }


    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }


    @GetMapping("get-donation-requests")
    public ResponseEntity<?> getDonationRequests(HttpServletRequest request) {
       try {
           String username = getUsername(request);
           List<DonationRequest> requests = donorService.getDonationRequests(username);
           return ResponseEntity.ok(requests);
       } catch (EntityNotFoundException e) {
          return ResponseEntity
                  .status(HttpStatus.NOT_FOUND)
                  .body(new ErrorResponse(e.getMessage()));
       }
    }

}
