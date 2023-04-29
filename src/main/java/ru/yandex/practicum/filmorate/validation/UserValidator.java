package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    public static ValidationResult validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return new ValidationResult(false, "Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            return new ValidationResult(false, "логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            return new ValidationResult(false, "дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return new ValidationResult(true, null);
    }
}
