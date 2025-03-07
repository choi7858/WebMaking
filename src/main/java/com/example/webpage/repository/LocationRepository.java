package com.example.webpage.repository;

import com.example.webpage.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findTopByOrderByIdDesc();
}
