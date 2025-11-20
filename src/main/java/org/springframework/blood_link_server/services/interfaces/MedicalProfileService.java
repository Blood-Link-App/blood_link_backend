package org.springframework.blood_link_server.services.interfaces;


import org.springframework.blood_link_server.models.dtos.requests.ProfileRequest;
import org.springframework.blood_link_server.models.dtos.requests.UpdateProfileRequest;
import org.springframework.blood_link_server.models.medicalProfile.MedicalProfile;

import java.util.UUID;

public interface MedicalProfileService {

    MedicalProfile createMedicalProfile (String username, ProfileRequest profileRequest);

    MedicalProfile getProfileById(String username, UUID id);

    MedicalProfile updateMedicalProfile(String username, UpdateProfileRequest updateRequest);
}
