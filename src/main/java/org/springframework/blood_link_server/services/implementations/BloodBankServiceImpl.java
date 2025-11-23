package org.springframework.blood_link_server.services.implementations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.repositories.BankStockRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.services.interfaces.BloodBankService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BloodBankServiceImpl implements BloodBankService {

    private final BloodBankRepository bloodBankRepository;
    private final BankStockRepository bankStockRepository;

    /**
     * Initialize blood bank stocks.
     */
    @Override
    public void initializeBloodBankStocks() {
        List <BloodBank> bloodBanks = bloodBankRepository.findBloodBanksByStockIsNull();

        for (BloodBank bank : bloodBanks ){
            BloodBankStock stock = new BloodBankStock();
            stock.setTotalQuantity(0);

            BloodBankStock savedStock = bankStockRepository.saveAndFlush(stock);

            bank.setStock(savedStock);
            bloodBankRepository.saveAndFlush(bank);
        }

    }

    /**
     * @param username of the blood bank
     * @param bloodType type of blood
     * @return stock of type blood type
     */
    @Override
    public StockByType getStockByType(String username, BloodType bloodType) {

        BloodBank bank = bloodBankRepository.findByEmail(username).orElse(null);

        if(bank == null){
            throw new EntityNotFoundException("Bank not found");
        }

        BloodBankStock bankStock = bank.getStock();

        if(bankStock == null) {
            throw new RuntimeException("This bank has no stock yet");
        }
        return bankStock.getStockByTypeList()
                .stream()
                .filter(s -> s.getBloodType() == bloodType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No stock found for blood type " + bloodType)
                );

    }

    /**
     * @param username fghjkl
     * @return fghjkl;
     */
    @Override
    public BloodBankStock getBloodBankStockByUsername(String username) {
        return Objects.requireNonNull(bloodBankRepository.findByEmail(username).orElse(null)).getStock();
    }

}
