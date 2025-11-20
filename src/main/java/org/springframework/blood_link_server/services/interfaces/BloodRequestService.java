package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.dtos.requests.BloodDemandRequest;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface
BloodRequestService {

    List<BloodRequest> sendRequests(String username, BloodDemandRequest request);

   // Set<BloodRequest> sendRequests1(String username, BloodDemandRequest request);

    List<BloodRequest> getPendingBloodRequests(RequestStatus status);

    //List<BloodRequest> getPendingBloodRequestsByIdAndStatus(RequestStatus status);

    List<BloodRequest> getPendingBloodRequestsByIdAndStatus(UUID bankId, RequestStatus status);

    BloodRequest processingBloodRequest(String username, UUID requestId);

}
