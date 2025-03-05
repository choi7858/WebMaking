package com.example.webpage.repository;

import com.example.webpage.model.Esg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EsgRepository extends JpaRepository<Esg, Long> {
    Optional<Esg> findTopByOrderByIdDesc();
}
