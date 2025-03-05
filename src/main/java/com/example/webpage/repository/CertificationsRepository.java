package com.example.webpage.repository;

import com.example.webpage.model.Certifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationsRepository extends JpaRepository<Certifications, Long> {
    Optional<Certifications> findTopByOrderByIdDesc();
}
