package com.example.webpage.controller;

import com.example.webpage.model.WaterSystem;
import com.example.webpage.repository.WaterSystemRepository;
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
@RequestMapping("/api/business/water-system")
@CrossOrigin(origins = "http://localhost:5173")
public class WaterSystemController {

    @Autowired
    private WaterSystemRepository waterSystemRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveWaterSystem(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            WaterSystem waterSystem = waterSystemRepository.findTopByOrderByIdDesc().orElse(new WaterSystem());

            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            waterSystem.setDescription(updatedDescription);

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

                        // 기존 파일 삭제 후 새 파일 저장
                        if (waterSystem.getImageUrl() != null && !waterSystem.getImageUrl().isEmpty()) {
                            String oldImagePath = waterSystem.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        waterSystem.setImageUrl("/" + filePath);
                    }
                } else {
                    waterSystem.setImageUrl("");
                }
            } else {
                String updatedImageUrl = (waterSystem.getImageUrl() != null && !waterSystem.getImageUrl().isEmpty())
                        ? waterSystem.getImageUrl()
                        : "";
                waterSystem.setImageUrl(updatedImageUrl);
            }

            WaterSystem savedWaterSystem = waterSystemRepository.save(waterSystem);
            return ResponseEntity.ok(savedWaterSystem);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<WaterSystem> getWaterSystem() {
        return ResponseEntity.ok(waterSystemRepository.findTopByOrderByIdDesc().orElse(new WaterSystem()));
    }
}
