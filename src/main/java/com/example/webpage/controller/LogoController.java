package com.example.webpage.controller;

import com.example.webpage.model.Logo;
import com.example.webpage.repository.LogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/logo")
@CrossOrigin(origins = "http://localhost:5173") // Vue 개발서버 허용
public class LogoController {

    @Autowired
    private LogoRepository logoRepository;

    // 업로드 폴더 (상대 경로)
    private static final String UPLOAD_DIR = "uploads/";
    // 개발 환경에서 이미지 접근할 서버 주소
    private static final String BASE_URL = "http://localhost:8080";

    // 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadLogo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("{\"error\": \"파일이 없습니다.\"}");
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"파일 이름이 없습니다.\"}");
            }

            // 고유 파일명 생성
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(originalFileName);

            // uploads 폴더 없으면 생성
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 저장 경로
            Path filePath = uploadPath.resolve(fileName);

            // 기존 로고 삭제
            Logo existingLogo = logoRepository.findTopByOrderByIdDesc();
            if (existingLogo != null && existingLogo.getUrl() != null) {
                // 기존 로고 URL에서 파일명 추출
                String oldUrl = existingLogo.getUrl();
                int idx = oldUrl.indexOf("/uploads/");
                if (idx != -1) {
                    String oldFileName = oldUrl.substring(idx + "/uploads/".length());
                    Path oldFilePath = Paths.get(UPLOAD_DIR).resolve(oldFileName);
                    Files.deleteIfExists(oldFilePath);
                }
                logoRepository.delete(existingLogo);
            }

            // 새 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // DB에 저장할 절대 URL
            String fileUrl = BASE_URL + "/uploads/" + fileName;

            Logo newLogo = new Logo();
            newLogo.setUrl(fileUrl);
            logoRepository.save(newLogo);

            // {"imageUrl": "..."} 형태로 반환
            return ResponseEntity.ok("{\"imageUrl\": \"" + fileUrl + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"파일 저장 중 오류 발생: " + e.getMessage() + "\"}");
        }
    }

    // 최신 로고 가져오기
    @GetMapping
    public ResponseEntity<?> getLogo() {
        Optional<Logo> logoOpt = Optional.ofNullable(logoRepository.findTopByOrderByIdDesc());
        // 없으면 기본 로고(절대 경로)라고 가정
        String defaultLogo = BASE_URL + "/assets/default-logo.png";
        String url = logoOpt.map(Logo::getUrl).orElse(defaultLogo);

        return ResponseEntity.ok("{\"imageUrl\": \"" + url + "\"}");
    }

    // 로고 삭제 (선택)
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLogo() {
        Logo existingLogo = logoRepository.findTopByOrderByIdDesc();
        if (existingLogo != null && existingLogo.getUrl() != null) {
            try {
                String oldUrl = existingLogo.getUrl();
                int idx = oldUrl.indexOf("/uploads/");
                if (idx != -1) {
                    String oldFileName = oldUrl.substring(idx + "/uploads/".length());
                    Path oldFilePath = Paths.get(UPLOAD_DIR).resolve(oldFileName);
                    Files.deleteIfExists(oldFilePath);
                }
                logoRepository.delete(existingLogo);
                return ResponseEntity.ok("{\"message\": \"로고 삭제 완료\"}");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"파일 삭제 중 오류 발생: " + e.getMessage() + "\"}");
            }
        }
        return ResponseEntity.ok("{\"message\": \"저장된 로고가 없습니다.\"}");
    }
}
