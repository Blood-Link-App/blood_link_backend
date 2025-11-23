package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;
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
     * @param username the current bloodbank connected
     * @param request is the dto necessary to create a stock by type
     * @return the updated BloodBank
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
     * @param username of the connected blood bank
     * @param quantity to increase the quantity of  current stock
     * @param stockId  the stock to increase in the stock
     * @return the increased stock
     */
    @Override
    public StockByType increaseQuantity(String username, Long quantity, UUID stockId) throws AuthException {
        BloodBank bank = bankRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        StockByType stock1 = typeRepository.findById(stockId).orElseThrow(() -> new UsernameNotFoundException("Not found bb"));
        long stockCount =  bank.getStock().getStockByTypeList().stream().filter(s -> s== stock1).count();
        if(stockCount == 0){
            throw new AuthException("not authorized");
        }
        if (quantity <= 0) throw new IllegalStateException("negative quantity");
        StockByType stock = typeRepository.findById(stockId).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        stock.upgradeQuantity(quantity);

        StockByType saved = typeRepository.saveAndFlush(stock);

        updateTotalQuantity(username);
        return saved;
    }





    /**
     * @param username of the connected blood bank
     * @param quantity to decrease the quantity of  current stock
     * @param stockId  the stock to decrease in the stock
     * @return the decreased stock
     */
    @Override
    public StockByType decreaseQuantity(String username, Long quantity, UUID stockId) throws AuthException {
        BloodBank bank = bankRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        StockByType stock = typeRepository.findById(stockId).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        long stockCount =  bank.getStock().getStockByTypeList().stream().filter(s -> s== stock).count();
        if(stockCount == 0){
            throw new AuthException("not authorized");
        }
        if (quantity >= 0) throw new IllegalStateException("positive quantity");
        if(-quantity > stock.getQuantity()) throw new ArrayStoreException("out of range");
        stock.upgradeQuantity(quantity);

        StockByType saved = typeRepository.save(stock);
        updateTotalQuantity(username);
        return saved;
    }

    /**
     * @return the new quantity in stock
     */




    @Override
    public long updateTotalQuantity(String username) {
        BloodBank bank  = bankRepository.findByEmail(username).orElseThrow(() ->new UsernameNotFoundException("User not found"));
        BloodBankStock stock = bank.getStock();
        stock.updateTotalQuantity();
        bankStockRepository.save(stock);
        return stock.getTotalQuantity();
    }

    /**
     * @param stockId id of the bank stock
     * @param bloodType of the stock by type
     * @return a stock by type
     */

    @Override
    public StockByType getStockBYtype(UUID stockId, BloodType bloodType) {

        BloodBankStock bankStock = bankStockRepository.findById(stockId).orElse(null);

        if (bankStock == null){
            throw new EntityNotFoundException("Bank not found");
        }
        return bankStock.getStockByTypeList()
                .stream()
                .filter(s -> s.getBloodType() == bloodType)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("No stock found for blood type " + bloodType));
    }


}
