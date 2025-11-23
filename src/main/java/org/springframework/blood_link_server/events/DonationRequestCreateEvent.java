package org.springframework.blood_link_server.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonationRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class DonationRequestCreateEvent {
    DonationRequest donationRequest;
}
