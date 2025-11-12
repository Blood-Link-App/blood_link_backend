package org.springframework.blood_link_server.repositories;

import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface BloodBankRepository extends JpaRepository<BloodBank, UUID> {
    Optional<BloodBank> findByBloodBankName(String bloodBankName);

    Optional<BloodBank> findByEmail(String email);

    Optional<BloodBank> findBloodBanksById(UUID id);

    List<BloodBank> findBloodBanksByStockIsNull();
}
