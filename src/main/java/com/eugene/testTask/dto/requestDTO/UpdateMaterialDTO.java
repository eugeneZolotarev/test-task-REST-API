package com.eugene.testTask.dto.requestDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateMaterialDTO (
        @Nullable
        @Size(min = 2, max = 100, message = "Название материала должно быть от 2 до 100 символов")
        String name,

        @Nullable
        @Positive(message = "Плотность должна быть положительным числом")
        Double density,

        @Nullable
        @Positive(message = "Модуль упругости должно быть положительным числом")
        Double elasticModulus
) {
}