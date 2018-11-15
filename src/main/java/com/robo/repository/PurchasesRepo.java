package com.robo.repository;

import com.robo.Entities.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PurchasesRepo extends JpaRepository<Purchases, Integer> {

    Optional<Purchases> findByUserIdAndShopIdAndProductIdAndDate (String userId, Integer shopId, Integer productId, LocalDate date);

}
