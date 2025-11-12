package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.blood_link_server.models.enumerations.BloodType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "stock_by_type")

public class StockByType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID typeId;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void upgradeQuantity(Integer quantity){
        this.quantity += quantity;
    }
}
