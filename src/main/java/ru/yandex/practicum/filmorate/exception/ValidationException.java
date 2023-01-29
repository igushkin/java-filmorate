package ru.yandex.practicum.filmorate.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
