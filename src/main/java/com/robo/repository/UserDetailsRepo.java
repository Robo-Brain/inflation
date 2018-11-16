package com.robo.repository;

import com.robo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<User, String> {

    @Override
    Optional<User> findById(String s);

    List<User> findByName(String name);

}
