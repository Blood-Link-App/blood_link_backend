package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.dtos.requests.ProfileRequest;
import org.springframework.blood_link_server.models.dtos.requests.QuestionsRequest;
import org.springframework.blood_link_server.models.dtos.requests.UpdateProfileRequest;
import org.springframework.blood_link_server.models.enumerations.Gender;
import org.springframework.blood_link_server.models.medicalProfile.*;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.repositories.*;
import org.springframework.blood_link_server.services.interfaces.MedicalProfileService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.blood_link_server.models.enumerations.Gender.FEMALE;
import static org.springframework.blood_link_server.models.enumerations.Gender.MALE;

@Service
@RequiredArgsConstructor

public class MedicalProfileServiceImpl implements MedicalProfileService {

    private final DonorRepository donorRepository;
    private final MedicalProfileRepository profileRepository;
    private final PersonalInfosRepository infosRepository;
    private final VitalSignRepository signRepository;
    private final HealthQuestionsRepository healthQuestionsRepository;



    /**
     * @param username of the connected donor
     * @param profileRequest information needed to create a medical profile
     * @return medical profile
     */
    @Override
    public MedicalProfile createMedicalProfile(String username, ProfileRequest profileRequest) {

        if (!verifyCreate(profileRequest)) {
            return null;
        }

        Donor donor = donorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("there's no user with this email " + username));


        PersonalInfos infos = new PersonalInfos(
                profileRequest.infosRequest().birthdate(),
                profileRequest.infosRequest().gender(),
                profileRequest.infosRequest().weight(),
                profileRequest.infosRequest().emergencyContact()
        );


        VitalSigns signs = new VitalSigns(
                profileRequest.SignsRequest().hemoglobinLevel(),
                profileRequest.SignsRequest().bodyTemperature(),
                profileRequest.SignsRequest().pulseRate()
        );

        HealthQuestions questions = getHealthQuestions(profileRequest);

        MedicalProfile medicalProfile = null;
        if (donor.getMedicalProfile() == null) {
            medicalProfile = MedicalProfile.builder()
                    .personalInfos(infos)
                    .healthQuestions(questions)
                    .vitalSigns(signs)
                    .build();
            donor.setMedicalProfile(medicalProfile);
        }

        assert medicalProfile != null;
        profileRepository.save(medicalProfile);
        donorRepository.save(donor);
        return profileRepository.save(medicalProfile);

    }

    private static HealthQuestions getHealthQuestions(ProfileRequest profileRequest) {
        QuestionsRequest hQ = profileRequest.questionsRequest();

        HealthQuestions questions;
        Gender gender = profileRequest.infosRequest().gender();


        if(gender.name().equals(FEMALE.name()))
        {
            questions = new HealthFemaleQuestions(
                    hQ.hasTattoosWithinLast6Months(),
                    hQ.hasSurgeryWithinLast6_12Months(),
                    hQ.hasChronicalIllness(),
                    hQ.hasTravelledWithinLast3Months(),
                    hQ.hasPiercingWithinLast7Months(),
                    hQ.isOnMedication(),
                    hQ.isPregnant(),
                    hQ.isBreastFeeding(),
                    hQ.isChildBirthWithinLast3Months(),
                    hQ.hasHeavyMenstrualFlow(),
                    hQ.lastMenstrualPeriod()
            );
        } else {
            questions = new HealthQuestions(
                    hQ.hasTattoosWithinLast6Months(),
                    hQ.hasSurgeryWithinLast6_12Months(),
                    hQ.hasChronicalIllness(),
                    hQ.hasTravelledWithinLast3Months(),
                    hQ.hasPiercingWithinLast7Months(),
                    hQ.isOnMedication()
            );
        }
        return questions;
    }


    private static boolean verifyCreate(ProfileRequest profile){
  ///      Gender gender = profile.infosRequest().gender();

        boolean checkSigns = profile.SignsRequest().bodyTemperature()  != 0
                && profile.SignsRequest().hemoglobinLevel() != 0
                && profile.SignsRequest().pulseRate() != 0 ;

        boolean checkInfos = profile.infosRequest().birthdate() != null
                && profile.infosRequest().emergencyContact() != null
                && profile.infosRequest().weight() != 0
                && profile.infosRequest().gender() != null;

        boolean generalQuestionsCheck = profile.questionsRequest().hasTattoosWithinLast6Months() != null
                && profile.questionsRequest().hasSurgeryWithinLast6_12Months() != null
                && profile.questionsRequest().hasChronicalIllness() != null;


        return checkInfos && checkSigns && generalQuestionsCheck;
    }



    /**
     * @param id of the donor
     * @return the medical profile of the donor
     */

    @Override
    public MedicalProfile getProfileById(String username, UUID id) {

        Donor donor = donorRepository.findById(id).orElse(null);

        if(donor == null)
            throw new EntityNotFoundException("Donor not found");

        if (!username.equals(donor.getUsername())){
            throw new AccessDeniedException("Unauthorized access to donor profile");
        }



        MedicalProfile profile = profileRepository.findById(donor.getMedicalProfile().getId())
                .orElse(null);

        if(profileRepository.findById(donor.getMedicalProfile().getId()).isEmpty()){
            return null;
        }

        assert profile != null;

        return profile;
        
    }

    /**
     * @param username of the connected donor
     * @param updateRequest up-to-date information
     * @return the updated profile
     */


    @Override
    public MedicalProfile updateMedicalProfile(String username, UpdateProfileRequest updateRequest) {

        Donor donor = donorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("donor not found"));


        MedicalProfile donorProfile = profileRepository.findById(donor.getMedicalProfile().getId())
                .orElse(null);

        if (donorProfile == null){
            return null;
        }

        PersonalInfos infos = donorProfile.getPersonalInfos();
        if(updateRequest.birthdate() != null) infos.setBirthdate(updateRequest.birthdate());
        if(updateRequest.gender() != null) infos.setGender(updateRequest.gender());
        if(updateRequest.weight() != 0) infos.setWeight(updateRequest.weight());
        if(updateRequest.emergencyContact() != null) infos.setEmergencyContact(updateRequest.emergencyContact());
        infosRepository.save(infos);


        VitalSigns signs = donorProfile.getVitalSigns();
        if(updateRequest.hemoglobinLevel() != 0) signs.setHemoglobinLevel(updateRequest.hemoglobinLevel());
        if(updateRequest.bodyTemperature() != 0) signs.setBodyTemperature(updateRequest.bodyTemperature());
        if(updateRequest.pulseRate() != 0) signs.setPulseRate(updateRequest.pulseRate());
        signRepository.save(signs);


        HealthQuestions questions = null;

        assert updateRequest.gender() != null;

        if(updateRequest.gender().equals(FEMALE)){
            HealthFemaleQuestions questions_temp = (HealthFemaleQuestions) donorProfile.getHealthQuestions();
            extracted(updateRequest, questions_temp);
            if(updateRequest.isPregnant() != null) questions_temp.setPregnant(updateRequest.isPregnant());
            if(updateRequest.isBreastFeeding() != null) questions_temp.setBreastFeeding(updateRequest.isBreastFeeding());
            if(updateRequest.hasHeavyMenstrualFlow() != null) questions_temp.setHasHeavyMenstrualFlow(updateRequest.hasHeavyMenstrualFlow());
            if(updateRequest.isChildBirthWithinLast3Months() != null) questions_temp.setChildBirthWithinLast3Months(updateRequest.isChildBirthWithinLast3Months());
            if(updateRequest.lastMenstrualPeriod() != null) questions_temp.setLastMenstrualPeriod(updateRequest.lastMenstrualPeriod());
            questions = questions_temp;
        }
        if(updateRequest.gender().equals(MALE)){
            HealthMaleQuestions questions_temp = (HealthMaleQuestions) donorProfile.getHealthQuestions();
            extracted(updateRequest, questions_temp);
            questions = questions_temp;
        }

        healthQuestionsRepository.save(questions);


/*        HealthQuestions questions_temp = switch (updateRequest.gender()){

            case MALE ->  new HealthQuestions(

                updateRequest.hasTattoosWithinLast6Months(),
                updateRequest.hasSurgeryWithinLast6_12Months(),
                updateRequest.hasChronicalIllness(),
                updateRequest.hasTravelledWithinLast3Months(),
                updateRequest.hasPiercingWithinLast7Months(),
                updateRequest.isOnMedication()
            );

            case FEMALE -> new HealthFemaleQuestions(
                    updateRequest.hasTattoosWithinLast6Months(),
                    updateRequest.hasSurgeryWithinLast6_12Months(),
                    updateRequest.hasChronicalIllness(),
                    updateRequest.hasTravelledWithinLast3Months(),
                    updateRequest.hasPiercingWithinLast7Months(),
                    updateRequest.isOnMedication(),
                    updateRequest.isPregnant(),
                    updateRequest.isBreastFeeding(),
                    updateRequest.hasHeavyMenstrualFlow(),
                    updateRequest.isChildBirthWithinLast3Months(),
                    updateRequest.lastMenstrualPeriod()
            );
        };*/

        donorProfile.setPersonalInfos(infos);
        donorProfile.setVitalSigns(signs);
        donorProfile.setHealthQuestions(questions);

        return profileRepository.save(donorProfile);
    }

    private static void extracted(UpdateProfileRequest updateRequest, HealthQuestions questions) {

        if (updateRequest.hasTattoosWithinLast6Months() != null)  questions.setHasTattoosWithinLast6Months(updateRequest.hasTattoosWithinLast6Months());
        if (updateRequest.hasSurgeryWithinLast6_12Months() != null) questions.setHasSurgeryWithinLast6_12Months(updateRequest.hasSurgeryWithinLast6_12Months());
        if (updateRequest.hasChronicalIllness() != null) questions.setHasChronicalIllness(updateRequest.hasChronicalIllness());
        if(updateRequest.hasTravelledWithinLast3Months() != null) questions.setHasTravelledWithinLast3Months(updateRequest.hasTravelledWithinLast3Months());
        if(updateRequest.hasPiercingWithinLast7Months() != null) questions.setHasPiercingWithinLast7Months(updateRequest.hasPiercingWithinLast7Months());
        if(updateRequest.isOnMedication() != null) questions.setOnMedication(updateRequest.isOnMedication());
    }
}

