package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoodsRepo extends JpaRepository<Goods, Integer> {

    Optional<Goods> findByName(String name);

    @Query("SELECT g FROM Goods g ORDER BY g.name ASC")
    List<Goods> findAllOrderByNameAsc();

    List<Goods> findByNameStartingWith(String letter);

//    @Query("SELECT g FROM Goods g WHERE g.name LIKE CONCAT('%',:letter,'%')")
//    List<Goods> findAllStartsWith(@Param("letter") String letter);

}
