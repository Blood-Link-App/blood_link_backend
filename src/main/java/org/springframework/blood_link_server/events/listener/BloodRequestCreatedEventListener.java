package org.springframework.blood_link_server.events.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.events.BloodRequestCreatedEvent;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BloodRequestCreatedEventListener {

    final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void onBloodRequestCreated(BloodRequestCreatedEvent event) {
        BloodRequest request = event.getBloodRequest();

        if (request.getBloodBank() != null)
            messagingTemplate.convertAndSendToUser(
                    request.getBloodBank().getUsername(),
                    "/queue/notifications", request
            );

    }

}
