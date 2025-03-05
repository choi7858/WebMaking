package com.example.webpage.repository;

import com.example.webpage.model.BusinessStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessStatusRepository extends JpaRepository<BusinessStatus, Long> {
    Optional<BusinessStatus> findTopByOrderByIdDesc();
}
