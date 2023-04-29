package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService service) {
        this.filmService = service;
    }

    @GetMapping
    public List<Film> getAll() {
        return this.filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable("filmId") Integer filmId) {
        return filmService.getById(filmId);
    }

    @PostMapping
    public Film create(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.filmService.create(film);
        return film;
    }

    @PutMapping
    public Film update(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        filmService.update(film);

        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        filmService.deleteLike(id, userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getTopRated(HttpServletRequest request, @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return filmService.getTopRated(count);
    }
}
