package org.springframework.blood_link_server.models.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.blood_link_server.models.enumerations.BloodType;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class StockByTypeRequest {
    private BloodType bloodType;
    private Integer quantity;
}
