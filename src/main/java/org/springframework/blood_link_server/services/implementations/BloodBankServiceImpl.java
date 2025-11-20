package org.springframework.blood_link_server.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.repositories.BankStockRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.services.interfaces.BloodBankService;
import org.springframework.stereotype.Service;

import java.util.List;

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

            BloodBankStock savedStock = bankStockRepository.save(stock);

            bank.setStock(savedStock);
            bloodBankRepository.save(bank);
        }

    }
}
