package ru.yandex.practicum.filmorate.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResult {
    private boolean isValid;
    private String message;
}
