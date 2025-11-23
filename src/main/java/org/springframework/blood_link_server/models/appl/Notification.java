package org.springframework.blood_link_server.models.appl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.blood_link_server.events.NotificationCreatedEvent;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notification")

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notification_id;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    private Alert alert;

    @ManyToOne
    @JsonIgnore
    private Donor donor;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "donor_response_id")
    private DonorResponse donorResponse;

    @DomainEvents
    public List<Object> domainEvents() {
        return List.of(new NotificationCreatedEvent(this));
    }

    @AfterDomainEventPublication
    public void callback() {

    }
}
