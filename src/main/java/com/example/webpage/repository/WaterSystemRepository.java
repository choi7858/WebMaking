package com.example.webpage.repository;

import com.example.webpage.model.WaterSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaterSystemRepository extends JpaRepository<WaterSystem, Long> {
    Optional<WaterSystem> findTopByOrderByIdDesc();
}
