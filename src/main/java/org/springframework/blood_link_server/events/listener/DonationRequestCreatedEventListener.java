package org.springframework.blood_link_server.events.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.events.DonationRequestCreateEvent;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DonationRequestCreatedEventListener {

    final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void onDonationRequestCreated(DonationRequestCreateEvent event){
        DonationRequest donationRequest = event.getDonationRequest();

        if(donationRequest.getBloodBank() != null)
            messagingTemplate.convertAndSendToUser(
                    donationRequest.getBloodBank().getUsername(),
                    "/queue/notifications", donationRequest
            );

        if(donationRequest.getDonor() != null)
            messagingTemplate.convertAndSendToUser(
                    donationRequest.getDonor().getUsername(),
                    "/queue/notifications", donationRequest
            );

    }
}

