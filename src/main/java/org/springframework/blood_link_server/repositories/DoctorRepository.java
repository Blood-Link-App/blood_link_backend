package org.springframework.blood_link_server.repositories;

import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findDoctorByEmail(String email);
}
