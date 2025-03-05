package com.example.webpage.controller;

import com.example.webpage.model.RemoteMonitoring;
import com.example.webpage.repository.RemoteMonitoringRepository;
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
@RequestMapping("/api/business/remote-monitoring")
@CrossOrigin(origins = "http://localhost:5173")
public class RemoteMonitoringController {

    @Autowired
    private RemoteMonitoringRepository remoteMonitoringRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveRemoteMonitoring(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            RemoteMonitoring remoteMonitoring = remoteMonitoringRepository.findTopByOrderByIdDesc()
                    .orElse(new RemoteMonitoring());

            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            remoteMonitoring.setDescription(updatedDescription);

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
                        if (remoteMonitoring.getImageUrl() != null && !remoteMonitoring.getImageUrl().isEmpty()) {
                            String oldImagePath = remoteMonitoring.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        remoteMonitoring.setImageUrl("/" + filePath);
                    }
                } else {
                    remoteMonitoring.setImageUrl("");
                }
            } else {
                String updatedImageUrl = (remoteMonitoring.getImageUrl() != null
                        && !remoteMonitoring.getImageUrl().isEmpty())
                                ? remoteMonitoring.getImageUrl()
                                : "";
                remoteMonitoring.setImageUrl(updatedImageUrl);
            }

            RemoteMonitoring savedRemoteMonitoring = remoteMonitoringRepository.save(remoteMonitoring);
            return ResponseEntity.ok(savedRemoteMonitoring);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<RemoteMonitoring> getRemoteMonitoring() {
        return ResponseEntity.ok(remoteMonitoringRepository.findTopByOrderByIdDesc().orElse(new RemoteMonitoring()));
    }
}
