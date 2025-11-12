package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.repositories.BankStockRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.StockByTypeRepository;
import org.springframework.blood_link_server.services.interfaces.BankStockService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.blood_link_server.services.implementations.StockByTypeServiceImpl.verifyCreate;

@Service
@RequiredArgsConstructor

public class BankStockServiceImpl implements BankStockService {

    private final BloodBankRepository bankRepository;
    private final BankStockRepository bankStockRepository;
    private final StockByTypeRepository typeRepository;

    /**
     * @param username
     * @param request
     * @return
     */
    @Override
    public BloodBankStock createStockByType(String username, StockByTypeRequest request) {

        if(!verifyCreate(request))
            return null;

        BloodBank bank = bankRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND"));

        StockByType stock = StockByType.builder()
                .quantity(request.getQuantity())
                .bloodType(request.getBloodType())
                .build();

        BloodBankStock bankStock = bank.getStock();
        bankStock.addStockByType(stock);

        return bankRepository.save(bank).getStock();
    }

    /**
     * @param quantity
     * @param stockId
     * @return
     */
    @Override
    public StockByType increaseQuantity(Integer quantity, UUID stockId) {
        if (quantity <= 0) throw new IllegalStateException("negative quantity");
        StockByType stock = typeRepository.findById(stockId).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        stock.upgradeQuantity(quantity);

        return typeRepository.save(stock);
    }

    /**
     * @param quantity
     * @param stockId
     * @return
     */
    @Override
    public StockByType decreaseQuantity(Integer quantity, UUID stockId) {
        if (quantity >= 0) throw new IllegalStateException("positive quantity");
        StockByType stock = typeRepository.findById(stockId).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        stock.upgradeQuantity(-quantity);

        return typeRepository.save(stock);
    }

    /**
     * @return
     */
    @Override
    public int updateTotalQuantity(String username) {

        BloodBank bank  = bankRepository.findByEmail(username).orElseThrow(() ->new UsernameNotFoundException("User not found"));
        BloodBankStock stock = bank.getStock();
        stock.updateTotalQuantity();
        bankStockRepository.save(stock);
        return stock.getTotalQuantity();
    }




}
