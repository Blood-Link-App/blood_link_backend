package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.dtos.requests.ProfileRequest;
import org.springframework.blood_link_server.models.dtos.requests.UpdateProfileRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.models.medicalProfile.MedicalProfile;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.blood_link_server.services.interfaces.MedicalProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/medical-profile")

@RequiredArgsConstructor
public class ProfileController {

    private final MedicalProfileService profileService;
    private final JwtService jwtService;

    @PostMapping("create-profile")

    public ResponseEntity<?> createMedicalProfile (HttpServletRequest httpServlet, @RequestBody ProfileRequest profileRequest)
    {
        try {
            String username = getUsername(httpServlet);
            MedicalProfile profile = profileService.createMedicalProfile(username, profileRequest);
            return  ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    @GetMapping("get-profile/id={donorId}")
    public ResponseEntity<?> getProfile(HttpServletRequest httpServlet, @PathVariable UUID donorId) {

        try {
            String username = getUsername(httpServlet);

            MedicalProfile profile = profileService.getProfileById(username, donorId);
            return ResponseEntity.ok(profile);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e);
        }
    }


    @PutMapping("update-profile")
    public ResponseEntity<?> updateProfile(HttpServletRequest httpServlet, @RequestBody UpdateProfileRequest updateRequest) {
        try{
            String username = getUsername(httpServlet);
            MedicalProfile updatedProfile = profileService.updateMedicalProfile(username, updateRequest);
            return ResponseEntity.ok(updatedProfile);
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
