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
            final String authHeader = httpRequest.getHeader("Authorization");
            var jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);

            BloodBankStock bankStock = bankStockService.createStockByType(username, stockByTypeRequest);
            if (bankStock == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid Entries!"));

            return ResponseEntity.ok(bankStock);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("increase/id={stockId}/quantity={quantity}")
    public ResponseEntity<?> upgradeQuantity(@PathVariable Integer quantity, @PathVariable UUID stockId) {
        try {
            StockByType stockByType = quantity >= 0 ? bankStockService.increaseQuantity(quantity, stockId) : bankStockService.decreaseQuantity(quantity, stockId);
            return ResponseEntity.ok(stockByType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }


    @GetMapping("/update-total-quantity")
    public ResponseEntity<?> updateTotalQuantity(HttpServletRequest http) {
        try {
            final String authHeader = http.getHeader("Authorization");
            var jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);

            Integer quantity = bankStockService.updateTotalQuantity(username);
            return ResponseEntity.ok(quantity);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

}
