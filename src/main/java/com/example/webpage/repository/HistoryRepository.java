package com.example.webpage.repository;

import com.example.webpage.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findTopByOrderByIdDesc();
}
