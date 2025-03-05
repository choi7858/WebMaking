package com.example.webpage.repository;

import com.example.webpage.model.Scada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScadaRepository extends JpaRepository<Scada, Long> {
    Optional<Scada> findTopByOrderByIdDesc();
}
