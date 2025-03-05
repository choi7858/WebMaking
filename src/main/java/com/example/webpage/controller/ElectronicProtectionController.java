package com.example.webpage.controller;

import com.example.webpage.model.ElectronicProtection;
import com.example.webpage.repository.ElectronicProtectionRepository;
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
@RequestMapping("/api/rnd/electronic-protection")
@CrossOrigin(origins = "http://localhost:5173")
public class ElectronicProtectionController {

    @Autowired
    private ElectronicProtectionRepository electronicProtectionRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveElectronicProtection(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ElectronicProtection electronicProtection = electronicProtectionRepository.findTopByOrderByIdDesc()
                    .orElse(new ElectronicProtection());

            electronicProtection.setDescription(description);

            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null) {
                    String fileName = StringUtils.cleanPath(originalFileName);
                    if (!fileName.isEmpty()) {
                        String filePath = UPLOAD_DIR + fileName;
                        File directory = new File(UPLOAD_DIR);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        if (electronicProtection.getImageUrl() != null
                                && !electronicProtection.getImageUrl().isEmpty()) {
                            String oldImagePath = electronicProtection.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        electronicProtection.setImageUrl("/" + filePath);
                    }
                }
            }

            ElectronicProtection savedElectronicProtection = electronicProtectionRepository.save(electronicProtection);
            return ResponseEntity.ok(savedElectronicProtection);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ElectronicProtection> getElectronicProtection() {
        return ResponseEntity
                .ok(electronicProtectionRepository.findTopByOrderByIdDesc().orElse(new ElectronicProtection()));
    }
}
