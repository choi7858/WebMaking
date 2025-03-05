package com.example.webpage.repository;

import com.example.webpage.model.CoilLift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoilLiftRepository extends JpaRepository<CoilLift, Long> {
    Optional<CoilLift> findTopByOrderByIdDesc();
}
