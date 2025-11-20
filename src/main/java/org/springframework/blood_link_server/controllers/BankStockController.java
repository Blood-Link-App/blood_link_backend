package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.services.interfaces.BankStockService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/bank-stock")
@RequiredArgsConstructor
public class BankStockController {

    private final JwtService jwtService;
    public final BankStockService bankStockService;

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(HttpServletRequest httpRequest, @RequestBody StockByTypeRequest stockByTypeRequest) {
        try {
            var username = getUsername(httpRequest);

            BloodBankStock bankStock = bankStockService.createStockByType(username, stockByTypeRequest);
            if (bankStock == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid Entries!"));

            return ResponseEntity.ok(bankStock);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("update-stock/id={stockId}/quantity={quantity}")
    public ResponseEntity<?> upgradeQuantity(HttpServletRequest httpRequest, @PathVariable long quantity, @PathVariable UUID stockId) {
        try {
            var username = getUsername(httpRequest);
            StockByType stockByType = quantity >= 0 ? bankStockService.increaseQuantity(username, quantity, stockId) : bankStockService.decreaseQuantity(username, quantity, stockId);
            return ResponseEntity.ok(stockByType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/update-total-quantity")
    public ResponseEntity<?> updateTotalQuantity(HttpServletRequest http) {
        try {
            var username = getUsername(http);

            long quantity = bankStockService.updateTotalQuantity(username);
            return ResponseEntity.ok(quantity);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }

}
