package org.springframework.blood_link_server.repositories;

import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface StockByTypeRepository extends JpaRepository<StockByType, UUID> {

    Optional<StockByType> findStockByBloodType(BloodType bloodType);

    int countStockByBloodType(BloodType bloodType);

    //List<StockByType> findAllByBloodType(List<BloodType> bloodType);

    List<StockByType> findByBloodTypeIn(List<BloodType> bloodTypes);

    /*   // List<StockByType> findAllByBloodType(BloodType bloodType);

    //List<StockByType> findAllByBloodType(List<BloodType> types);*/

//    @Query("SELECT bs FROM BloodBankStock bs INNER JOIN  StockByType st ON bs.id = st.stock_type_id ")
//    Optional<BloodBankStock> findBloodBankByStockId(UUID stockId);
}
