package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsRepo extends JpaRepository<Goods, Integer> {

    Optional<Goods> findByName(String name);

    @Query("SELECT g FROM Goods g ORDER BY g.name ASC")
    List<Goods> findAllOrderByNameAsc();

   List<Goods> findByNameContainingIgnoreCase("к");

//     @Query("SELECT g FROM Goods AS g WHERE UPPER(g.name) LIKE CONCAT('%',UPPER('К'),'%')")
//     List<Goods> findByNameStartingWith(@Param("letter") String letter);

}
