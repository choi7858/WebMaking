package com.example.webpage.repository;

import com.example.webpage.model.ElectricWorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectricWorkStatusRepository extends JpaRepository<ElectricWorkStatus, Long> {
    Optional<ElectricWorkStatus> findTopByOrderByIdDesc();
}
