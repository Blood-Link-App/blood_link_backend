package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.metiers.Donor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DonorService {

    void affiliateDonorToBloodBank(String username, UUID bankId);

    List<DonationRequest> getDonationRequests(String username);

    Optional<Donor> getDonorByEmail(String email);

    // TODO: create a controller for this method
    void removeDonorAffiliationToBloodBank(UUID bankId);

    void affiliateDonorToBloodBank(String name);

    void removeDonorAffiliationToBloodBank(String name);

}
