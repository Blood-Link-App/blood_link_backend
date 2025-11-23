package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.DonationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "donation")

public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID donationId;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "units_gave", nullable = false)
    private long unitsGave;

    @Column(name = "donation_status")
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "is_validated", nullable = false)
    private boolean isValidated;

    @CreationTimestamp
    @Column(name = "donation_date", nullable = false, updatable = false)
    private LocalDateTime donationDate;
}
