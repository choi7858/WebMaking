package com.example.webpage.repository;

import com.example.webpage.model.BuildingAutomation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingAutomationRepository extends JpaRepository<BuildingAutomation, Long> {
    Optional<BuildingAutomation> findTopByOrderByIdDesc();
}
