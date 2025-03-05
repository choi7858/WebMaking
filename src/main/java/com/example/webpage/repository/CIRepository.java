package com.example.webpage.repository;

import com.example.webpage.model.CIData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CIRepository extends JpaRepository<CIData, Long> {
    Optional<CIData> findTopByOrderByIdDesc();
}
