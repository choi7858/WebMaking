package com.example.webpage.controller;

import com.example.webpage.model.SSILS;
import com.example.webpage.repository.SSILSRepository;
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
@RequestMapping("/api/rnd/ssils")
@CrossOrigin(origins = "http://localhost:5173")
public class SSILSController {

    @Autowired
    private SSILSRepository ssilsRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveSSILS(
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            SSILS ssils = ssilsRepository.findTopByOrderByIdDesc().orElse(new SSILS());

            String updatedDescription = (description != null && !description.isEmpty()) ? description : "";
            ssils.setDescription(updatedDescription);

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

                        if (ssils.getImageUrl() != null && !ssils.getImageUrl().isEmpty()) {
                            String oldImagePath = ssils.getImageUrl().replaceFirst("/", "");
                            File oldFile = new File(oldImagePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                            }
                        }

                        Path targetPath = Paths.get(filePath);
                        Files.deleteIfExists(targetPath);
                        Files.copy(file.getInputStream(), targetPath);

                        ssils.setImageUrl("/" + filePath);
                    }
                }
            }

            SSILS savedSSILS = ssilsRepository.save(ssils);
            return ResponseEntity.ok(savedSSILS);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<SSILS> getSSILS() {
        return ResponseEntity.ok(ssilsRepository.findTopByOrderByIdDesc().orElse(new SSILS()));
    }
}
