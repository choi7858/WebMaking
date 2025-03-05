package com.example.webpage.repository;

import com.example.webpage.model.ElectronicProtection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectronicProtectionRepository extends JpaRepository<ElectronicProtection, Long> {
    Optional<ElectronicProtection> findTopByOrderByIdDesc();
}
