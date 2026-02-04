package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.MaterialRequestDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> createMaterial(
            @Valid @RequestBody MaterialRequestDTO materialDTO) {
        return ResponseEntity.ok(materialService.createMaterial(materialDTO));
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDTO>> getAll() {
        return ResponseEntity.ok(materialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody MaterialRequestDTO materialDTO) {
        return ResponseEntity.ok(materialService.updateMaterial(id, materialDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
    }
}
