package com.example.webpage.repository;

import com.example.webpage.model.SSILS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SSILSRepository extends JpaRepository<SSILS, Long> {
    Optional<SSILS> findTopByOrderByIdDesc();
}
