package com.example.webpage.controller;

import com.example.webpage.model.Notice;
import com.example.webpage.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notice")
@CrossOrigin(origins = "http://localhost:5173")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadNotice(@RequestBody Notice notice) {
        notice.setCreatedAt(LocalDateTime.now());
        Notice savedNotice = noticeRepository.save(notice);
        return ResponseEntity.ok(savedNotice);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Notice>> getNotices(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return ResponseEntity.ok(notices);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable(name = "id") Long id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return ResponseEntity.ok("공지사항이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(404).body("삭제할 공지사항이 존재하지 않습니다.");
        }
    }
}
