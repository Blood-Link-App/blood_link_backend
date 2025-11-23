package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.metiers.Donor;

import java.util.Optional;
import java.util.UUID;

public interface DonorService {
    void affiliateDonorToBloodBank(String username, UUID bankId);
    Optional<Donor> getDonorByEmail(String email);
    void removeDonorAffiliationToBloodBank(UUID bankId);

    void affiliateDonorToBloodBank(String name);

    void removeDonorAffiliationToBloodBank(String name);

}
