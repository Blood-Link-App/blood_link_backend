package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface DonorService {
    void affiliateDonorToBloodBank(String username, UUID bankId);
    Optional<Donor> getDonorByEmail(String email);
    void removeDonorAffiliationToBloodBank(UUID bankId);

    void affiliateDonorToBloodBank(String name);

    void removeDonorAffiliationToBloodBank(String name);

    // void affiliateDonorToBloodBank(String name);
   // void removeDonorAffiliationToBloodBank(String name);
}
