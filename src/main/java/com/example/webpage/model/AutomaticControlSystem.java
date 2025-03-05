package com.example.webpage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticControlSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "분류는 필수입니다.")
    private String type;

    @NotEmpty(message = "계약명은 필수입니다.")
    private String contractName;

    @NotEmpty(message = "제작 및 설치기간은 필수입니다.")
    private String manufactureYear;

    @NotEmpty(message = "계약처 및 수요처는 필수입니다.")
    private String client;
}