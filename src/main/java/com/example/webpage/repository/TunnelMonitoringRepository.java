package com.example.webpage.repository;

import com.example.webpage.model.TunnelMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TunnelMonitoringRepository extends JpaRepository<TunnelMonitoring, Long> {
    Optional<TunnelMonitoring> findTopByOrderByIdDesc();
}
