package com.example.webpage.repository;

import com.example.webpage.model.CeoMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CeoMessageRepository extends JpaRepository<CeoMessage, Long> {
    Optional<CeoMessage> findTopByOrderByIdDesc();
}
