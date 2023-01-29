package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();
    private int id = 1;

    @GetMapping
    public List<Film> getGroups() {
        return this.films;
    }

    @PostMapping
    public Film create(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        validate(film);
        film.setId(this.id);
        this.id++;
        this.films.add(film);
        return film;
    }

    @PutMapping
    public Film update(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        validate(film);

        if (!films.contains(film)) {
            log.error("Фильм не найден в базе данных: " + film);
            throw new ValidationException("Фильм не найден в базе");
        }

        this.films.remove(film);
        this.films.add(film);
        return film;
    }

    private void validate(Film film) {
        ValidationResult validationResult = FilmValidator.validate(film);
        if (!validationResult.isValid()) {
            log.error("Передан невалидный объект фильма: " + film);
            throw new ValidationException(validationResult.getMessage());
        }
    }
}
