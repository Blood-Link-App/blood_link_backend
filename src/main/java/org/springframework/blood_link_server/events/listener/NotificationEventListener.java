package org.springframework.blood_link_server.events.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.events.NotificationCreatedEvent;
import org.springframework.blood_link_server.models.appl.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor

public class NotificationEventListener {

    final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void onNotificationCreated(NotificationCreatedEvent event) {
        Notification notification = event.getNotification();
        if (notification.getDonor() != null) {
            messagingTemplate.convertAndSendToUser(
                    notification.getDonor().getUsername(),
                    "/queue/notifications",
                    notification
            );
            messagingTemplate.convertAndSendToUser(
                    notification.getAlert().getBloodRequest().getBloodBank().getUsername(),
                    "/queue/notifications",
                    notification
            );
        }
    }
}
