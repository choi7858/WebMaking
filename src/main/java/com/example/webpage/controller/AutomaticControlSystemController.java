package com.example.webpage.controller;

import com.example.webpage.model.AutomaticControlSystem;
import com.example.webpage.repository.AutomaticControlSystemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/performance/automatic-control-system")
@CrossOrigin(origins = "http://localhost:5173", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS })
public class AutomaticControlSystemController {

    private static final Logger logger = LoggerFactory.getLogger(AutomaticControlSystemController.class);
    private final AutomaticControlSystemRepository repository;

    public AutomaticControlSystemController(AutomaticControlSystemRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/upload")
    public ResponseEntity<AutomaticControlSystem> upload(@Valid @RequestBody AutomaticControlSystem system) {
        logger.info("Uploading new automatic control system: {}", system);
        try {
            AutomaticControlSystem savedSystem = repository.save(system);
            logger.info("Successfully saved system with ID: {}", savedSystem.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSystem);
        } catch (Exception e) {
            logger.error("Failed to upload automatic control system: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<AutomaticControlSystem>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        logger.info("Fetching automatic control systems, page: {}, size: {}", page, size);
        try {
            Page<AutomaticControlSystem> systems = repository
                    .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
            logger.debug("Fetched systems content: {}", systems.getContent());
            if (systems.isEmpty()) {
                logger.warn("No automatic control systems found for page: {}, size: {}", page, size);
            }
            return ResponseEntity.ok(systems);
        } catch (Exception e) {
            logger.error("Failed to fetch automatic control systems, page: {}, size: {}, error: {}", page, size,
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AutomaticControlSystem> update(@PathVariable Long id,
            @Valid @RequestBody AutomaticControlSystem system) {
        logger.info("Updating automatic control system with ID: {}", id);
        try {
            AutomaticControlSystem existingSystem = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("해당 ID의 데이터를 찾을 수 없습니다."));
            existingSystem.setType(system.getType());
            existingSystem.setContractName(system.getContractName());
            existingSystem.setManufactureYear(system.getManufactureYear());
            existingSystem.setClient(system.getClient());
            AutomaticControlSystem updatedSystem = repository.save(existingSystem);
            logger.info("Successfully updated system with ID: {}", updatedSystem.getId());
            return ResponseEntity.ok(updatedSystem);
        } catch (NoSuchElementException e) {
            logger.error("Automatic control system not found for ID: {}, error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to update automatic control system with ID: {}, error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) { // 매개변수 이름 명시
        logger.info("Deleting automatic control system with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                logger.info("Successfully deleted system with ID: {}", id);
                return ResponseEntity.ok("자동제어시스템이 삭제되었습니다.");
            } else {
                logger.warn("Automatic control system not found for ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 자동제어시스템이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            logger.error("Failed to delete automatic control system with ID: {}, error: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}