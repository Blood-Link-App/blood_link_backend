package org.springframework.blood_link_server.models.appl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.blood_link_server.events.DonationRequestCreateEvent;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
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
@Table(name = "donation_request")

public class DonationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne(cascade = CascadeType.ALL)
    private Donor donor;

    @ManyToOne(cascade = CascadeType.ALL)
    private BloodBank bloodBank;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "donation_id")
    private Donation donation;

    @DomainEvents
    public List<Object> sendNotif() {return List.of(new DonationRequestCreateEvent(this));}

    @AfterDomainEventPublication
    public void afterAll(){}
}


