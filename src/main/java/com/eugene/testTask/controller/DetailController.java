package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.DetailRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateDetailDTO;
import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.enums.DetailStatus;
import com.eugene.testTask.service.DetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/details")
@Tag(name = "Детали", description = "Методы для работы с каталогом деталей")
public class DetailController {
    private final DetailService detailService;

    @PostMapping
    @Operation(
            summary = "Создание новой записи",
            description = "Создает запись в каталоге о детали и возвращает ее"
    )
    public ResponseEntity<DetailResponseDTO> createDetail(
            @Valid @RequestBody DetailRequestDTO detailDTO) {
        return new ResponseEntity<>(detailService.createDetail(detailDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Получение списка всех деталей",
            description = "Возвращает список со всеми деталями в каталоге, учитывается пагинация"
    )
    public ResponseEntity<Page<DetailResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(detailService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Деталь по его id",
            description = "Возвращает информацию о детали по ее id"
    )
    public ResponseEntity<DetailResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detailService.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновление записи о детали",
            description = "Частично обновляет информацию о детали по ее id и возвращает ее. Допускается в запросе не писать значения для всех полей, если они не изменяются"
    )
    public ResponseEntity<DetailResponseDTO> updateDetail(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) UpdateDetailDTO detailDTO) {
        return ResponseEntity.ok(detailService.updateDetail(id, detailDTO));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Изменение статуса",
            description = "Изменяет статус детали по ее id, не допускается изменять статус детали, находящейся в состоянии ARCHIVED"
    )
    public void updateStatus(@PathVariable Long id, @RequestParam DetailStatus detailStatus) {
        detailService.updateStatus(id, detailStatus);
    }
}
