package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;
import org.springframework.blood_link_server.models.metiers.Donor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="notification")

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notification_id;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private Alert alert;

    @ManyToOne
    private Donor donor;

    @OneToOne
    @JoinColumn(name = "donor_response_id")
    private DonorResponse donorResponse;

        /*    @DomainEvents
    public List<Object> domainEvents() {
        return List.of(new NotificationCreatedEvent(this));
    }

    @AfterDomainEventPublication
    public void callback() {

    }*/
}
