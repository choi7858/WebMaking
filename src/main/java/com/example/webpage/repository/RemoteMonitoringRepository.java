package com.example.webpage.repository;

import com.example.webpage.model.RemoteMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemoteMonitoringRepository extends JpaRepository<RemoteMonitoring, Long> {
    Optional<RemoteMonitoring> findTopByOrderByIdDesc();
}
