package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.blood_link_server.models.enumerations.AlertStatus;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.metiers.BloodBank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "alert")

public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID alertId;

    @CreationTimestamp
    @Column(name= "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

    @OneToOne
    private BloodBank bloodBank;

    private Integer quantity;

    private List<BloodType> compatibleTypes ;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Notification> notifications;


/*
    @OneToOne(cascade = CascadeType.ALL)
    private BloodRequest bloodRequest;


    @Column(name = "message", nullable = false)
    private String message;
*/

}
