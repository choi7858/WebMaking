package com.example.webpage.repository;

import com.example.webpage.model.KHNP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KHNPRepository extends JpaRepository<KHNP, Long> {
    Optional<KHNP> findTopByOrderByIdDesc();
}
