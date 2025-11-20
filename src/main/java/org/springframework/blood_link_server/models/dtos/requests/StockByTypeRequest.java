package org.springframework.blood_link_server.models.dtos.requests;

import lombok.*;
import org.springframework.blood_link_server.models.enumerations.BloodType;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class StockByTypeRequest {
    private BloodType bloodType;
    private Long quantity;

/*    public StockByTypeRequest(BloodType bloodType, Long quantity){
        this.bloodType = bloodType;
        this.quantity = quantity;
    }*/
}
