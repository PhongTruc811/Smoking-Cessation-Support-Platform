package com.group_7.backend.controller;

import com.group_7.backend.dto.QuitMethodDto;
import com.group_7.backend.dto.response.ResponseDto; // Import ResponseDto của bạn
import com.group_7.backend.service.IQuitMethodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quit-methods")
public class QuitMethodController {

    private final IQuitMethodService quitMethodService;

    public QuitMethodController(IQuitMethodService quitMethodService) {
        this.quitMethodService = quitMethodService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createQuitMethod(@Valid @RequestBody QuitMethodDto quitMethodDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "QuitMethod created successfully", quitMethodService.create(quitMethodDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getQuitMethodById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "QuitMethod fetched successfully", quitMethodService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllQuitMethods() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All QuitMethods fetched successfully", quitMethodService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateQuitMethod(@PathVariable Long id, @Valid @RequestBody QuitMethodDto quitMethodDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "QuitMethod updated successfully", quitMethodService.update(id, quitMethodDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteQuitMethod(@PathVariable Long id) {
        quitMethodService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "QuitMethod deleted successfully", null)
        );
    }
}