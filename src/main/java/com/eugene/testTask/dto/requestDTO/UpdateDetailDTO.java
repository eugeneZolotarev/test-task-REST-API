package com.eugene.testTask.dto.requestDTO;

import com.eugene.testTask.enums.DetailStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record UpdateDetailDTO (
        @Nullable
        @Size(min = 2, max = 100, message = "Название детали должно быть от 2 до 100 символов")
        String name,

        @Nullable
        @Pattern(regexp = "^[A-ZА-Я]{4}\\.\\d{6}\\.\\d{3}$",
                message = "Децимальный номер должен быть в формате АБВГ.123456.789")
        String decimalNumber,

        @Nullable
        Long materialId,

        @Nullable
        @Positive(message = "Вес детали должен быть положительным числом")
        Double weight,

        @Nullable
        DetailStatus detailStatus
) {
}
