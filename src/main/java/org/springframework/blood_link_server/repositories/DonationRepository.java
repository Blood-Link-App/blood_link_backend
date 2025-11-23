package org.springframework.blood_link_server.repositories;

import org.springframework.blood_link_server.models.appl.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository

public interface DonationRepository extends JpaRepository<Donation, UUID> {

}
