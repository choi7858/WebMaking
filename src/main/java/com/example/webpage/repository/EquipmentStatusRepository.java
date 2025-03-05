package com.example.webpage.repository;

import com.example.webpage.model.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EquipmentStatusRepository extends JpaRepository<EquipmentStatus, Long> {
    Optional<EquipmentStatus> findTopByOrderByIdDesc();
}
