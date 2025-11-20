package org.springframework.blood_link_server.models.appl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.blood_link_server.events.BloodRequestCreatedEvent;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;
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
@Table(name = "blood_requests")

public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "group_request_id", updatable = false)
    private UUID groupRequestId;


    @Enumerated(EnumType.STRING)
    @Column(name = "recipientType")
    private BloodType recipientType;


    @Column(name = "quantity_needed", nullable = false)
    private Long quantityNeeded;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    //Bidirectional (Many - Many) relationship between Doctor and Blood bank
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    private Doctor doctor;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private BloodBank bloodBank;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.DETACH)
    private Alert alert;

    @DomainEvents
    public List<Object> sendNotif(){
        return List.of(new BloodRequestCreatedEvent(this));
    }

    @AfterDomainEventPublication
    public void afterAll() {

    }

}
