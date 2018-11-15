package com.robo.repository;

import com.robo.Entities.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PurchasesRepo extends JpaRepository<Purchases, Integer> {

    Optional<Purchases> findByUserIdAndShopIdAndProductIdAndDate (String userId, Integer shopId, Integer productId, LocalDate date);
        
//     Optional<Purchases> findByShopIdStartingWith(Integer num);
    
   @Query("SELECT p FROM Purchases p WHERE p.shopId LIKE :num ||'%'")
   Optional<Purchases> findByShopIdStartingWith(@Param("num") Integer num);

}
