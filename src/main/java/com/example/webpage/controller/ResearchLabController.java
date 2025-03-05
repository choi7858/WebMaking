package com.example.webpage.controller;

import com.example.webpage.model.ResearchLab;
import com.example.webpage.repository.ResearchLabRepository;
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
@RequestMapping("/api/rnd/research-lab")
@CrossOrigin(origins = "http://localhost:5173")
public class ResearchLabController {

    @Autowired
    private ResearchLabRepository researchLabRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveResearchLab(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ResearchLab researchLab = researchLabRepository.findTopByOrderByIdDesc().orElse(new ResearchLab());
            researchLab.setDescription(description);

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

                        if (researchLab.getImageUrl() != null && !researchLab.getImageUrl().isEmpty()) {
                            String oldImagePath = researchLab.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        researchLab.setImageUrl("/" + filePath);
                    }
                }
            }

            ResearchLab savedResearchLab = researchLabRepository.save(researchLab);
            return ResponseEntity.ok(savedResearchLab);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ResearchLab> getResearchLab() {
        return ResponseEntity.ok(researchLabRepository.findTopByOrderByIdDesc().orElse(new ResearchLab()));
    }
}
