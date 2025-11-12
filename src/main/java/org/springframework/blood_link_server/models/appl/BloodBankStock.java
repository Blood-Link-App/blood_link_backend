package org.springframework.blood_link_server.models.appl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="blood_bank_stock")

public class BloodBankStock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;


    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name= "stock_type_id")
    private Set<StockByType> stockByTypeList = new HashSet<>();

    @Column(name= "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name= "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addStockByType(StockByType type){
        this.stockByTypeList.add(type);
    }

    public void updateTotalQuantity () {
        for (StockByType type : stockByTypeList) {
            totalQuantity += type.getQuantity();
        }
    }
}
