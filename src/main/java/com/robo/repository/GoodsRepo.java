package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GoodsRepo extends JpaRepository<Goods, Integer> {
//
//    List<Goods> findByName(String name);
//
//    List<Goods> findAllById(String id);
//
//    @Query("SELECT g FROM Goods g where g.name = :name")
//    List<Goods> findByNameQuery(@Param("name") String name);

    @Query("SELECT g FROM Goods g ORDER BY g.name ASC")
    List<Goods> findAllOrderByNameAsc();

    List<Goods> findByNameStartingWith(String letter);

    @Transactional
    void removeById(Integer id);
}
