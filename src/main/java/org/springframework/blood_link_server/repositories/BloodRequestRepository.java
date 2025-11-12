package org.springframework.blood_link_server.repositories;

import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, UUID> {
    List<BloodRequest> findBloodRequestsByStatus(RequestStatus status);

    List<BloodRequest> findByBloodBankAndStatus(BloodBank bloodBank, RequestStatus status);
}
