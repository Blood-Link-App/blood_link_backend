package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.medicalProfile.MedicalProfile;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.DonorRepository;
import org.springframework.blood_link_server.repositories.MedicalProfileRepository;
import org.springframework.blood_link_server.services.interfaces.DonationService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DonationServiceImpl implements DonationService {

    private final BloodBankRepository bankRepository;
    private final MedicalProfileRepository profileRepository;
    private final DonorRepository donorRepository;


    /**
     * @param username of blood bank
     * @param profileId of the donor's medical profile
     * @return boolean
     */

    @Override
    public boolean checkEligibility(String username, UUID profileId) {

        BloodBank bank = bankRepository.findByEmail(username).orElse(null);


        MedicalProfile profile = profileRepository.findById(profileId).orElse(null);

        Donor donor = donorRepository.findByMedicalProfile_Id(profileId);

        if(bank == null){
            throw new EntityNotFoundException("Blood bank not found");
        }

        if (profile == null) {
            throw new EntityNotFoundException("Profile not found");
        }

        if (!bank.getAffiliatedDonors().contains(donor)){
            throw new NoSuchElementException("The donor isn't affiliated to this bank");
        }

        return profile.getPersonalInfos().getAge() >= 18 && profile.getVitalSigns().meetDonationCriteria() && profile.getHealthQuestions().hasDeferralIssues();

    }
}
