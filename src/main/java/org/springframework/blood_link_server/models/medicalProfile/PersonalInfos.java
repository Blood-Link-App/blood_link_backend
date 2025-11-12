package org.springframework.blood_link_server.models.medicalProfile;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.blood_link_server.models.enumerations.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "personal_infos")

public class PersonalInfos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID infosId;

    @Column(name ="date_of_birth", nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public int getAge(){
        return birthdate.getYear()-LocalDate.now().getYear();
    }
}
