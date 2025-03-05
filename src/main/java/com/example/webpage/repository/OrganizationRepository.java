package com.example.webpage.repository;

import com.example.webpage.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findTopByOrderByIdDesc();
}
