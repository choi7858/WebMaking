package com.example.webpage.controller;

import com.example.webpage.model.TunnelMonitoring;
import com.example.webpage.repository.TunnelMonitoringRepository;
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
@RequestMapping("/api/business/tunnel-monitoring")
@CrossOrigin(origins = "http://localhost:5173")
public class TunnelMonitoringController {

    @Autowired
    private TunnelMonitoringRepository tunnelMonitoringRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveTunnelMonitoring(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            TunnelMonitoring tunnelMonitoring = tunnelMonitoringRepository.findTopByOrderByIdDesc()
                    .orElse(new TunnelMonitoring());

            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            tunnelMonitoring.setDescription(updatedDescription);

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
                        if (tunnelMonitoring.getImageUrl() != null && !tunnelMonitoring.getImageUrl().isEmpty()) {
                            String oldImagePath = tunnelMonitoring.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        tunnelMonitoring.setImageUrl("/" + filePath);
                    }
                } else {
                    tunnelMonitoring.setImageUrl("");
                }
            } else {
                String updatedImageUrl = (tunnelMonitoring.getImageUrl() != null
                        && !tunnelMonitoring.getImageUrl().isEmpty())
                                ? tunnelMonitoring.getImageUrl()
                                : "";
                tunnelMonitoring.setImageUrl(updatedImageUrl);
            }

            TunnelMonitoring savedTunnelMonitoring = tunnelMonitoringRepository.save(tunnelMonitoring);
            return ResponseEntity.ok(savedTunnelMonitoring);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<TunnelMonitoring> getTunnelMonitoring() {
        return ResponseEntity.ok(tunnelMonitoringRepository.findTopByOrderByIdDesc().orElse(new TunnelMonitoring()));
    }
}
