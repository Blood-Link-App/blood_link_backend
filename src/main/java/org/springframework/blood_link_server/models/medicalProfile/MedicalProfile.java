package org.springframework.blood_link_server.models.medicalProfile;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "medical_profile")

public class MedicalProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private PersonalInfos personalInfos;

    @OneToOne(cascade = CascadeType.PERSIST)
    private VitalSigns vitalSigns;

    @OneToOne(cascade = CascadeType.PERSIST)
    private HealthQuestions healthQuestions;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
