package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepo extends JpaRepository<Goods, Integer> {

    List<Goods> findByName(String name);

    @Query("SELECT g FROM Goods g where g.id = :id")
    List<Goods> findByIdQuery(@Param("id") Integer id);

    @Query("SELECT g FROM Goods g where g.name = :name")
    List<Goods> findByNameQuery(@Param("name") String name);

    @Query("SELECT g FROM Goods g ORDER BY g.name ASC")
    List<Goods> findAllOrderByNameAsc();

}
