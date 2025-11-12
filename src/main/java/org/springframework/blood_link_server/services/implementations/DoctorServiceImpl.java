package org.springframework.blood_link_server.services.implementations;

import jakarta.transaction.Transactional;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.DoctorRepository;
import org.springframework.blood_link_server.services.interfaces.DoctorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final BloodBankRepository bankRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, BloodBankRepository bankRepository) {
        this.doctorRepository = doctorRepository;
        this.bankRepository = bankRepository;
    }


    /**
     * @param bankId is the parameter used to affiliate a doctor to a blood bank
     */
    @Override
    public void affiliateDoctorToBloodBank(UUID bankId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Doctor doctor = doctorRepository.findDoctorByEmail(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        BloodBank bloodBank = bankRepository.findBloodBanksById(bankId)
                .orElseThrow(()->new RuntimeException("BloodBank not found"));
        doctor.getAffiliatedDoctorBanks().add(bloodBank);
    }

    /**
     * @param email is the parameter required for the method getDoctorByEmail
     * @return a Doctor or an Exception
     */
    @Override
    public Optional<Doctor> getDoctorByEmail(String email) {
        return Optional.of(doctorRepository.findDoctorByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
    }

    /**
     * @param bankId
     */
    @Override
    public void removeDoctorAffiliationToBloodBank(UUID bankId) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Doctor doctor = doctorRepository.findDoctorByEmail(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        BloodBank bloodBank = bankRepository.findBloodBanksById(bankId)
                .orElseThrow(()->new RuntimeException("BloodBank not found"));
        doctor.getAffiliatedDoctorBanks().remove(bloodBank);

    }

    /**
     *
     * return a list of doctor's affiliations
     */
    public Set<BloodBank> allAffiliations(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Doctor doctor = doctorRepository.findDoctorByEmail(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return doctor.getAffiliatedDoctorBanks();
    }


    /**
     * @param name
     */
    @Override
    public void affiliateDoctorToBloodBank(String name) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Doctor doctor = doctorRepository.findDoctorByEmail(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        BloodBank bloodBank = bankRepository.findByBloodBankName(name)
                .orElseThrow(()->new RuntimeException("BloodBank not found"));
        doctor.getAffiliatedDoctorBanks().add(bloodBank);
    }

    /**
     * @param name
     */
    @Override
    public void removeDoctorAffiliationToBloodBank(String name) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Doctor doctor = doctorRepository.findDoctorByEmail(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        BloodBank bloodBank = bankRepository.findByBloodBankName(name)
                .orElseThrow(()->new RuntimeException("BloodBank not found"));
        doctor.getAffiliatedDoctorBanks().remove(bloodBank);
    }
}
