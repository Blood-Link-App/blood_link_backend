package org.springframework.blood_link_server.models.medicalProfile;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "vital_signs")

public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hemoglobin_level", nullable = false)
    private float hemoglobinLevel;

    @Column(name = "body_temperature", nullable = false)
    private float bodyTemperature;

    @Column(name = "pulse_rate", nullable = false)
    private float pulseRate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public VitalSigns(float hemoglobinLevel, float bodyTemperature, float pulseRate) {
        this.bodyTemperature =  bodyTemperature;
        this.hemoglobinLevel = hemoglobinLevel;
        this.pulseRate = pulseRate;
    }


    public boolean meetDonationCriteria(){
        return (hemoglobinLevel >= 12.0) && (bodyTemperature <= 38) && (bodyTemperature >= 37) && (pulseRate <= 100) && (pulseRate >= 50);
    }
}
