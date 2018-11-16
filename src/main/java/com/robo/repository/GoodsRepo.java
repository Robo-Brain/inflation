package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsRepo extends JpaRepository<Goods, String> {

    List<Goods> findByName(String name);

    @Query("SELECT g FROM Goods g ORDER BY g.name ASC")
    List<Goods> findAllOrderByNameAsc();

}
