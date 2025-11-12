package org.springframework.blood_link_server.models.medicalProfile;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "health_questions")
@Inheritance(strategy = InheritanceType.JOINED)

public class HealthQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "has_tattoos_within_last_6Months", nullable = false)
    private boolean hasTattoosWithinLast6Months;

    @Column(name = "has_surgery_within_last6_12Months", nullable = false)
    private boolean hasSurgeryWithinLast6_12Months;

    @Column(name = "has_chronical_illness", nullable = false)
    private boolean hasChronicalIllness;

    @Column(name = "has_travelled_within_last_3Months", nullable = false)
    private boolean hasTravelledWithinLast3Months;

    @Column(name = "has_piercing_within_last_7Months", nullable = false)
    private boolean hasPiercingWithinLast7Months;

    @Column(name = "is_on_medication")
    private boolean isOnMedication;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public boolean hasDeferralIssues(){
        return hasTattoosWithinLast6Months || hasSurgeryWithinLast6_12Months || hasChronicalIllness || hasPiercingWithinLast7Months || hasTravelledWithinLast3Months || isOnMedication;
    }
}
