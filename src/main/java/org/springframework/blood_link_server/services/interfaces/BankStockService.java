package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public interface BankStockService {

    BloodBankStock createStockByType(String username, StockByTypeRequest request);

    StockByType increaseQuantity(Integer quantity, UUID stockId);

    StockByType decreaseQuantity(Integer quantity, UUID stockId);

    int updateTotalQuantity(String username);

}
