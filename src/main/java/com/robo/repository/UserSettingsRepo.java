package com.robo.repository;

import com.robo.Entities.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingsRepo extends JpaRepository<UserSettings, Integer> {

    Optional<UserSettings> findByUserId(String id);

}
