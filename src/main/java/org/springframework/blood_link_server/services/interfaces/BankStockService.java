package org.springframework.blood_link_server.services.interfaces;

import jakarta.security.auth.message.AuthException;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public interface BankStockService {

    BloodBankStock createStockByType(String username, StockByTypeRequest request);

    StockByType increaseQuantity(String username, Long quantity, UUID stockId) throws AuthException;

    StockByType decreaseQuantity(String username, Long quantity, UUID stockId) throws AuthException;

    long updateTotalQuantity(String username);

}
