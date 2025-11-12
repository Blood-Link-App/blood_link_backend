package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.StockByTypeRequest;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.repositories.StockByTypeRepository;
import org.springframework.blood_link_server.services.interfaces.StockByTypeService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockByTypeServiceImpl implements StockByTypeService {

    private final StockByTypeRepository typeRepository;
    private  BloodType bloodType;

    /**
     * @param request is the parameter sent to check if there's enough blood in the stock
     * @return a boolean or not
     */
    @Override
    public Boolean checkAvailability(StockByTypeRequest request) {
        if(!verifyCreate(request)){
            return null;
        }

        StockByType stock = typeRepository.findStockByBloodType(request.getBloodType())
                .orElseThrow(() -> new UsernameNotFoundException("Unavailable stock"));;


        return  (stock.getBloodType() == request.getBloodType() && stock.getQuantity() >= request.getQuantity());
    }

    /**
     * @param request is the parameter sent to check if there's enough blood in the stock
     * @return a boolean
     */
    @Override
    public Boolean checkAvailabilityWithCompatibility(StockByTypeRequest request) {
        List<BloodType> compatibleTypes = BloodType.getCompatibleDonors(request.getBloodType());
        int totalAvailable = 0;
        List<StockByType> stocks = typeRepository.findByBloodTypeIn(compatibleTypes);
        for (StockByType stock : stocks){
           totalAvailable = stock.getQuantity();
        }
        return totalAvailable  >= request.getQuantity();
    }

    /**
     * @return the quantity in stock by type
     */
    @Override
    public int getQuantityByType(BloodType bloodType) {
        return typeRepository.countStockByBloodType(bloodType);
    }

    /**
     *
     */
    @Override
    public void addInStock() {

    }

    /**
     *
     */
    @Override
    public void removeInStock() {

    }

    public static boolean verifyCreate(StockByTypeRequest request){
        return request.getQuantity()!= null && request.getQuantity() > 0
                && request.getBloodType() != null;
    }
}
