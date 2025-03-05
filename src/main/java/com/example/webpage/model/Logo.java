package com.example.webpage.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "logo")
public class Logo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실제로는 DB에 절대 URL을 저장할 예정 (ex: http://localhost:8080/uploads/파일명)
    @Column(nullable = false, unique = true)
    private String url;
}
