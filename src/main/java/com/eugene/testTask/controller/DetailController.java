package com.eugene.testTask.controller;

import com.eugene.testTask.dto.requestDTO.DetailRequestDTO;
import com.eugene.testTask.dto.requestDTO.UpdateDetailDTO;
import com.eugene.testTask.dto.responseDTO.DetailResponseDTO;
import com.eugene.testTask.enums.DetailStatus;
import com.eugene.testTask.service.DetailService;
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
public class DetailController {
    private final DetailService detailService;

    @PostMapping
    public ResponseEntity<DetailResponseDTO> createDetail(
            @Valid @RequestBody DetailRequestDTO detailDTO) {
        return ResponseEntity.ok(detailService.createDetail(detailDTO));
    }

    @GetMapping
    public ResponseEntity<Page<DetailResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(detailService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detailService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DetailResponseDTO> updateDetail(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDetailDTO detailDTO) {
        return ResponseEntity.ok(detailService.updateDetail(id, detailDTO));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id, @RequestParam DetailStatus detailStatus) {
        detailService.updateStatus(id, detailStatus);
    }
}
