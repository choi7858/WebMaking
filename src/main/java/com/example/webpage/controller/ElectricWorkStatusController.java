package com.example.webpage.controller;

import com.example.webpage.model.ElectricWorkStatus;
import com.example.webpage.repository.ElectricWorkStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/performance/electric-work-status")
@CrossOrigin(origins = "http://localhost:5173")
public class ElectricWorkStatusController {

    @Autowired
    private ElectricWorkStatusRepository electricWorkStatusRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveElectricWorkStatus(
            @RequestParam(value = "content", required = false, defaultValue = "") String content,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 기존 데이터 불러오기
            ElectricWorkStatus electricWorkStatus = electricWorkStatusRepository.findTopByOrderByIdDesc()
                    .orElse(new ElectricWorkStatus());

            // ✅ NULL 방지: 기존 데이터 유지 또는 기본 값 설정
            electricWorkStatus.setContent(Optional.ofNullable(content).orElse(""));

            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    String fileName = StringUtils.cleanPath(originalFileName);
                    String filePath = UPLOAD_DIR + fileName;
                    File directory = new File(UPLOAD_DIR);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    // 기존 파일 삭제
                    Optional.ofNullable(electricWorkStatus.getImageUrl())
                            .filter(url -> !url.isEmpty())
                            .ifPresent(oldImage -> {
                                File oldFile = new File(oldImage.replaceFirst("/", ""));
                                if (oldFile.exists()) {
                                    oldFile.delete();
                                }
                            });

                    // 새 파일 저장
                    Path targetPath = Paths.get(filePath);
                    Files.deleteIfExists(targetPath);
                    Files.copy(file.getInputStream(), targetPath);

                    electricWorkStatus.setImageUrl("/" + filePath);
                }
            } else {
                // ✅ NULL 방지: 기존 이미지 유지 또는 빈 문자열 저장
                electricWorkStatus.setImageUrl(Optional.ofNullable(electricWorkStatus.getImageUrl()).orElse(""));
            }

            ElectricWorkStatus savedElectricWorkStatus = electricWorkStatusRepository.save(electricWorkStatus);
            return ResponseEntity.ok(savedElectricWorkStatus);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ElectricWorkStatus> getElectricWorkStatus() {
        return ResponseEntity
                .ok(electricWorkStatusRepository.findTopByOrderByIdDesc().orElse(new ElectricWorkStatus()));
    }
}
