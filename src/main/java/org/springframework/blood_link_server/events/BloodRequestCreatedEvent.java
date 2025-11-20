package org.springframework.blood_link_server.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestCreatedEvent {
    BloodRequest bloodRequest;
}

