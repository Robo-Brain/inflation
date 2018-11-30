package com.robo.repository;

import com.robo.Entities.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchasesRepo extends JpaRepository<Purchases, Integer> {

    List<Purchases> findAllByUserId(String id);

    @Query("SELECT p FROM Purchases p where p.userId = :userId and p.productId = :productId")
    List<Purchases> findAllByUserIdAndProductId(@Param("userId") String userId,
                                                @Param("productId") Integer productId);

    @Query("SELECT p FROM Purchases p where p.userId = :userId and p.shopId = :shopId and p.productId = :productId and p.date = :date")
    Optional<Purchases> findAllByUserIdAndShopIdAndProductIdAndDate (@Param("userId") String userId,
                                                                     @Param("shopId") Integer shopId,
                                                                     @Param("productId") Integer productId,
                                                                     @Param("date") LocalDate date);
    @Transactional
    void removeById(Integer id);

}
