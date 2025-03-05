package com.example.webpage.controller;

import com.example.webpage.model.Menu;
import com.example.webpage.repository.MenuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:5173") // Vue 서버 허용
public class MenuController {
    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // 모든 메뉴 불러오기
    @GetMapping
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    // 메뉴 저장 또는 업데이트
    @PostMapping
    public ResponseEntity<Menu> saveMenu(@RequestBody Menu menu) {
        Menu savedMenu = menuRepository.save(menu);
        return ResponseEntity.ok(savedMenu);
    }
}
