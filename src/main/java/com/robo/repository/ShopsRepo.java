package com.robo.repository;

import com.robo.Entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopsRepo extends JpaRepository<Shops, Integer> {
    Optional<Shops> findByName(String name);
    
//     List<Shops> findByNameStartingWith(String letter);
    
   @Query("SELECT s FROM Shops AS s WHERE s.name LIKE :letter%")
   List<Shops> findByNameStartingWith(@Param("letter") String letter);
}
