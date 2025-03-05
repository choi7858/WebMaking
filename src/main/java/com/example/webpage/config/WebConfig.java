package com.example.webpage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull; // Spring의 NonNull 어노테이션 임포트

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") // 모든 API 엔드포인트에 적용
                .allowedOrigins("http://localhost:5173", "http://localhost:8080") // Vue 앱 출처 허용 (프론트엔드 포트)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키 허용 (필요 시)
                .maxAge(3600); // Preflight 요청 캐시 시간 (초)
    }
}