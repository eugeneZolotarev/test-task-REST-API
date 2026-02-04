package com.eugene.testTask.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MaterialRequestDTO (
        @NotBlank(message = "Название материала обязано быть")
        @Size(min = 2, max = 100, message = "Название материала должно быть от 2 до 100 символов")
        String name,

        @NotNull(message = "Плотность обязана быть указана")
        @Positive(message = "Плотность должна быть положительным числом")
        Double density,

        @NotNull(message = "Модуль упругости обязан быть указан")
        @Positive(message = "Модуль упругости должно быть положительным числом")
        Double elasticModulus
) {
}
