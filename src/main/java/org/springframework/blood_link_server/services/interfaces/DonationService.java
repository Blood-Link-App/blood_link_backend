package org.springframework.blood_link_server.services.interfaces;

import java.util.UUID;

public interface DonationService {

    boolean checkEligibility(String username, UUID profile);

}
