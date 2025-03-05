package com.example.webpage.controller;

import com.example.webpage.model.LightingControl;
import com.example.webpage.repository.LightingControlRepository;
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
@RequestMapping("/api/business/lighting-control")
@CrossOrigin(origins = "http://localhost:5173")
public class LightingControlController {

    @Autowired
    private LightingControlRepository lightingControlRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveLightingControl(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            LightingControl lightingControl = lightingControlRepository.findTopByOrderByIdDesc()
                    .orElse(new LightingControl());

            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            lightingControl.setDescription(updatedDescription);

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
                        if (lightingControl.getImageUrl() != null && !lightingControl.getImageUrl().isEmpty()) {
                            String oldImagePath = lightingControl.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        lightingControl.setImageUrl("/" + filePath);
                    }
                } else {
                    lightingControl.setImageUrl("");
                }
            } else {
                String updatedImageUrl = (lightingControl.getImageUrl() != null
                        && !lightingControl.getImageUrl().isEmpty())
                                ? lightingControl.getImageUrl()
                                : "";
                lightingControl.setImageUrl(updatedImageUrl);
            }

            LightingControl savedLightingControl = lightingControlRepository.save(lightingControl);
            return ResponseEntity.ok(savedLightingControl);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<LightingControl> getLightingControl() {
        return ResponseEntity.ok(lightingControlRepository.findTopByOrderByIdDesc().orElse(new LightingControl()));
    }
}
