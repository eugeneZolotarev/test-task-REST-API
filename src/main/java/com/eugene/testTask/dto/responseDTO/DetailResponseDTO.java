package com.eugene.testTask.dto.responseDTO;

import com.eugene.testTask.enums.DetailStatus;

import java.time.LocalDateTime;

public record DetailResponseDTO (
        Long id,
        String name,
        String decimalNumber,
        MaterialResponseDTO material,
        Double weight,
        DetailStatus detailStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
