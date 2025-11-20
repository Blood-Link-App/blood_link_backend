package org.springframework.blood_link_server.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.blood_link_server.models.appl.Notification;

@Getter
@AllArgsConstructor
public class NotificationCreatedEvent {
    Notification notification;
}
