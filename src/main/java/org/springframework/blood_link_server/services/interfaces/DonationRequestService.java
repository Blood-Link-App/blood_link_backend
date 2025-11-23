package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.Donation;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.dtos.requests.DonationDemandRequest;

import java.util.UUID;


public interface DonationRequestService {

    DonationRequest createDonationRequest(String username, UUID notifId, DonationDemandRequest demandRequest);

    Donation processDonationRequest(String username, UUID requestId);

}
