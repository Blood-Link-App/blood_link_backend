package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.DonorRepository;
import org.springframework.blood_link_server.services.interfaces.DonorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final BloodBankRepository bloodBankRepository;

    /**
     * @param bankId is the id of the bank the donor should affiliate to
     */
    @Override
    public void affiliateDonorToBloodBank(String username, UUID bankId) {
        String finalUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

//         = username;
        Donor donor = donorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("there's no donor with that " + finalUsername + " email"));

        BloodBank bloodBank = bloodBankRepository.findBloodBanksById(bankId)
                .orElseThrow(() -> new UsernameNotFoundException("Blood bank not found"));

        donor.affiliateNewBank(bloodBank);
        bloodBank.getAffiliatedDonors().add(donor);

        bloodBankRepository.save(bloodBank);

        donorRepository.save(donor);

    }

    /**
     * @return donation requests
     */

    @Override
    public List<DonationRequest> getDonationRequests(String username) {
        Donor donor = donorRepository.findByEmail(username).orElse(null);
        if (donor == null) {
            throw new UsernameNotFoundException("there's no donor with that " + username + " email");
        }
        return donor.getDonationRequests();
    }

    /**
     * @param email is the required parameter to find a donor by Email
     * @return the donor found using the email given as the parameter
     */
    @Override
    public Optional<Donor> getDonorByEmail(String email) {
        return Optional.of(donorRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Donor not found" + email));
    }

    /**
     * @param bankId is the id of the bank the donor should break affiliation with
     */
    @Override
    public void removeDonorAffiliationToBloodBank(UUID bankId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Donor donor = donorRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("there's no donor with that " + username + " email"));

        BloodBank bloodBank = bloodBankRepository.findBloodBanksById(bankId)
                .orElseThrow(() -> new RuntimeException("Blood bank not found"));

        bloodBank.getAffiliatedDonors().remove(donor);
        donor.removeAffiliateBank(bloodBank);

        bloodBankRepository.save(bloodBank);

        donorRepository.save(donor);


        donorRepository.save(donor);
        }



    /**
     * @param name of the blood bank
     */
    @Override
    public void affiliateDonorToBloodBank(String name) {

    }

    /**
     * @param name of the blood bank
     */
    @Override
    public void removeDonorAffiliationToBloodBank(String name) {

    }


}


