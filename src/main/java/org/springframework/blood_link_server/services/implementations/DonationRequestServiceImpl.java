package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.dtos.requests.DonationDemandRequest;
import org.springframework.blood_link_server.services.interfaces.DonationRequestService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class DonationRequestServiceImpl implements DonationRequestService {
    /**
     * @param username of the connected donor
     * @param demandRequest is the parameter needed to make a donation request
     * @return a donation request
     */
    @Override
    public DonationRequest createDonationRequest(String username, DonationDemandRequest demandRequest) {
        return null;
    }
}
