package com.example.webpage.repository;

import com.example.webpage.model.ElectricProtection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectricProtectionRepository extends JpaRepository<ElectricProtection, Long> {
    Optional<ElectricProtection> findTopByOrderByIdDesc();
}
