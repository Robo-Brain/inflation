package com.robo.repository;

import com.robo.Entities.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsRepo extends JpaRepository<Goods, Integer> {

    Optional<Goods> findByName(String name);

}
