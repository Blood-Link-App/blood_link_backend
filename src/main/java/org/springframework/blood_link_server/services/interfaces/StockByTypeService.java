package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;


public interface StockByTypeService {

    Boolean checkAvailability(StockByTypeRequest request);
    Boolean checkAvailabilityWithCompatibility(StockByTypeRequest request);
    int getQuantityByType(BloodType bloodType);
    void addInStock();
    void removeInStock();

}
