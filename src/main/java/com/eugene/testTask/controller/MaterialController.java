package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.MaterialRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateMaterialDTO;
import com.eugene.testTask.dto.responseDTO.MaterialResponseDTO;
import com.eugene.testTask.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/materials")
@RequiredArgsConstructor
@Tag(name = "Материалы", description = "Методы для работы с каталогом материалов")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @Operation(
            summary = "Создание новой записи",
            description = "Создает запись в каталоге о материале и возвращает ее"
    )
    public ResponseEntity<MaterialResponseDTO> createMaterial(
            @Valid @RequestBody MaterialRequestDTO materialDTO) {
        return new ResponseEntity<>(materialService.createMaterial(materialDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Получение списка всех материалов",
            description = "Возвращает список со всеми материалами в каталоге"
    )
    public ResponseEntity<List<MaterialResponseDTO>> getAll() {
        return ResponseEntity.ok(materialService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Материал по его id",
            description = "Возвращает информацию о материале по его id"
    )
    public ResponseEntity<MaterialResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновление записи о материале",
            description = "Частично обновляет информацию о материале по его id и возвращает ее"
    )
    public ResponseEntity<MaterialResponseDTO> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) UpdateMaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.updateMaterial(id, materialDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удаление материала",
            description = "Удаляет запись о материале по его id"
    )
    public void deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
    }
}
