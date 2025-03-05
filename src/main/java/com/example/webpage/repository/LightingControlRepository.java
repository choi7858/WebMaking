package com.example.webpage.repository;

import com.example.webpage.model.LightingControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightingControlRepository extends JpaRepository<LightingControl, Long> {
    Optional<LightingControl> findTopByOrderByIdDesc();
}
