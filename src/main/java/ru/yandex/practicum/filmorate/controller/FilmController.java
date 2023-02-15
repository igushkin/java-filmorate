package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getAll() {
        return this.service.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable("filmId") Integer filmId) {
        return service.getById(filmId);
    }

    @PostMapping
    public Film create(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.service.create(film);
        return film;
    }

    @PutMapping
    public Film update(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        service.update(film);

        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        service.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        service.deleteLike(id, userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getTopRated(HttpServletRequest request, @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return service.getTopRated(count);
    }
}
