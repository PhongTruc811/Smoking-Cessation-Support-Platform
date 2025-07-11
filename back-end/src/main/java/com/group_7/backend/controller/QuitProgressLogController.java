package com.group_7.backend.controller;

import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IQuitProgressLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/quit-progress")
public class QuitProgressLogController {

    private final IQuitProgressLogService quitProgressLogService;

    public QuitProgressLogController(IQuitProgressLogService quitProgressLogService) {
        this.quitProgressLogService = quitProgressLogService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody QuitProgressLogDto dto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit progress log created successfully", quitProgressLogService.create(dto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit progress log fetched successfully", quitProgressLogService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAll() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All quit progress logs fetched successfully", quitProgressLogService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@PathVariable Long id, @Valid @RequestBody QuitProgressLogDto dto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit progress log updated successfully", quitProgressLogService.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        quitProgressLogService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit progress log deleted successfully", null)
        );
    }
}
