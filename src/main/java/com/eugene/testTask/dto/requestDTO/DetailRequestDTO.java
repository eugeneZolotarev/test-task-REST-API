package com.eugene.testTask.dto.requestDTO;

import com.eugene.testTask.enums.DetailStatus;
import jakarta.validation.constraints.*;

public record DetailRequestDTO (
        @NotBlank(message = "Название детали обязательно")
        @Size(min = 2, max = 100, message = "Название детали должно быть от 2 до 100 символов")
        String name,

        @NotBlank(message = "Децимальный номер детали обязателен")
        @Pattern(regexp = "^[A-ZА-Я]{4}\\.\\d{6}\\.\\d{3}$",
                message = "Децимальный номер должен быть в формате АБВГ.123456.789")
        String decimalNumber,

        @NotNull(message = "Материал детали обязателен")
        Long materialId,

        @NotNull(message = "Вес детали обязателен")
        @Positive(message = "Вес детали должен быть положительным числом")
        Double weight,

        DetailStatus detailStatus
) {
}
