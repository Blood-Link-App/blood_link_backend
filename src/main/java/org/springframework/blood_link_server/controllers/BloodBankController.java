package org.springframework.blood_link_server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.services.interfaces.BloodBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/bloodbank")
@RequiredArgsConstructor

public class BloodBankController {

    private final BloodBankService bankService;

    @GetMapping("initialize-blood-bank-stocks")

    public ResponseEntity<String> post(){
        try {
            bankService.initializeBloodBankStocks();
            return ResponseEntity.ok("Blood bank stocks initialized successfully");
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initializing blood bank stocks: " + e.getMessage());
        }
    }
}
