package com.example.webpage.repository;

import com.example.webpage.model.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {
    // 가장 최근 로고 (마지막에 저장된) 하나를 조회
    Logo findTopByOrderByIdDesc();
}
