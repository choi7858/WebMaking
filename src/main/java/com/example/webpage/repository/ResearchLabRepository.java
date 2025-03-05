package com.example.webpage.repository;

import com.example.webpage.model.ResearchLab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchLabRepository extends JpaRepository<ResearchLab, Long> {
    Optional<ResearchLab> findTopByOrderByIdDesc();
}
