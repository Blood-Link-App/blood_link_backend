package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.dtos.requests.DonationDemandRequest;
import org.springframework.stereotype.Service;

@Service
public interface DonationRequestService {
    DonationRequest createDonationRequest(String username, DonationDemandRequest demandRequest);
}
