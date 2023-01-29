package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;
import ru.yandex.practicum.filmorate.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private List<User> users = new ArrayList<>();
    private int id = 1;

    @GetMapping
    public List<User> getGroups() {
        return this.users;
    }

    @PostMapping
    public User create(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        validate(user);
        user.setId(this.id);
        this.id++;
        this.users.add(user);
        return user;
    }

    @PutMapping
    public User update(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        validate(user);

        if (!users.contains(user)) {
            log.error("Пользователя не найден в базе данных: " + user);
            throw new ValidationException("Пользователь не найден в базе");
        }

        this.users.remove(user);
        this.users.add(user);
        return user;
    }

    private void validate(User user) {
        ValidationResult validationResult = UserValidator.validate(user);
        if (!validationResult.isValid()) {
            log.error("Передан невалидный объект пользователя: " + user);
            throw new ValidationException(validationResult.getMessage());
        }
    }
}
