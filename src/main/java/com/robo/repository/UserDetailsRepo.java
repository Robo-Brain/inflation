package com.robo.repository;

import com.robo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<User, String> {

    @Override
    Optional<User> findById(String s);
    
    Optional<User> findByNameStartingWith(String letter);
    
//    @Query("SELECT s FROM Shops AS s WHERE s.name LIKE :letter%")
//    Optional<Shops> findByNameStartingWith(@Param("letter") String letter);

}
