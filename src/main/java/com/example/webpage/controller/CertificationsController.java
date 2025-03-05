package com.example.webpage.controller;

import com.example.webpage.model.Certifications;
import com.example.webpage.repository.CertificationsRepository;
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

@RestController
@RequestMapping("/api/certifications")
@CrossOrigin(origins = "http://localhost:5173")
public class CertificationsController {

    @Autowired
    private CertificationsRepository certificationsRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveCertifications(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 기존 데이터 불러오기
            Certifications certifications = certificationsRepository.findTopByOrderByIdDesc()
                    .orElse(new Certifications());

            // ✅ NULL 방지: 기존 데이터 유지 또는 기본 값 설정
            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            certifications.setDescription(updatedDescription);

            if (file != null && !file.isEmpty()) {
                // 파일의 원본 이름을 먼저 가져와 null 여부 확인
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null) {
                    String fileName = StringUtils.cleanPath(originalFileName);
                    if (!fileName.isEmpty()) {
                        String filePath = UPLOAD_DIR + fileName;
                        File directory = new File(UPLOAD_DIR);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        // 기존 파일 삭제 후 새 파일 저장
                        if (certifications.getImageUrl() != null && !certifications.getImageUrl().isEmpty()) {
                            String oldImagePath = certifications.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        certifications.setImageUrl("/" + filePath);
                    }
                } else {
                    // 원본 파일 이름이 null인 경우 이미지 URL을 빈 문자열로 설정
                    certifications.setImageUrl("");
                }
            } else {
                // 파일이 없는 경우 기존 이미지 URL 유지 또는 빈 문자열로 설정
                String updatedImageUrl = (certifications.getImageUrl() != null
                        && !certifications.getImageUrl().isEmpty())
                                ? certifications.getImageUrl()
                                : "";
                certifications.setImageUrl(updatedImageUrl);
            }

            Certifications savedData = certificationsRepository.save(certifications);
            return ResponseEntity.ok(savedData);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Certifications> getCertifications() {
        return ResponseEntity.ok(certificationsRepository.findTopByOrderByIdDesc().orElse(new Certifications()));
    }
}
