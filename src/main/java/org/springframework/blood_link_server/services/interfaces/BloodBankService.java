package org.springframework.blood_link_server.services.interfaces;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.enumerations.BloodType;

public interface BloodBankService {

    void initializeBloodBankStocks();

    StockByType getStockByType(String username, BloodType bloodType);

   BloodBankStock getBloodBankStockByUsername(String username) throws EntityNotFoundException;
}
