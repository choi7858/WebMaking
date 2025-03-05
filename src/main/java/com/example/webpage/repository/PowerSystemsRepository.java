package com.example.webpage.repository;

import com.example.webpage.model.PowerSystems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PowerSystemsRepository extends JpaRepository<PowerSystems, Long> {
    Optional<PowerSystems> findTopByOrderByIdDesc();
}
