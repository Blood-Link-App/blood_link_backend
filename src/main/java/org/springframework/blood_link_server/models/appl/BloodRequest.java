package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;


import java.time.LocalDateTime;
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
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.ALL)
    private BloodBank bloodBank;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Alert alert;


}
