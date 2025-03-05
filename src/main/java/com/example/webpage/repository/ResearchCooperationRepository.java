package com.example.webpage.repository;

import com.example.webpage.model.ResearchCooperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchCooperationRepository extends JpaRepository<ResearchCooperation, Long> {
    Optional<ResearchCooperation> findTopByOrderByIdDesc();
}
