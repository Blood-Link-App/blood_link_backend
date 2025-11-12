package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service

public interface DoctorService {
    void affiliateDoctorToBloodBank(UUID bankId);
    Optional<Doctor> getDoctorByEmail(String email);
    void removeDoctorAffiliationToBloodBank(UUID bankId);

    void affiliateDoctorToBloodBank(String name);
    void removeDoctorAffiliationToBloodBank(String name);
}

