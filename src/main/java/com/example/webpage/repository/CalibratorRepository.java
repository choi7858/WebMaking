package com.example.webpage.repository;

import com.example.webpage.model.Calibrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalibratorRepository extends JpaRepository<Calibrator, Long> {
    Optional<Calibrator> findTopByOrderByIdDesc();
}
