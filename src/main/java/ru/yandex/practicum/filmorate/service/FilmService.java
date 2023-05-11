package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static int id;

    static {
        id = 1;
    }

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        log.info("Получен запрос к методу getAll() класса FilmService");
        return filmStorage.getAll();
    }

    public Film getById(int id) {
        log.info("Получен запрос к методу getById() класса FilmService, id: {}", id);
        return filmStorage.getById(id);
    }

    public Film create(Film film) {
        log.info("Получен запрос к методу create() класса FilmService, film: {}", film);
        validate(film);

        film.setId(this.id);
        filmStorage.create(film);
        this.id++;
        return film;
    }

    public Film update(Film film) {
        log.info("Получен запрос к методу update() класса FilmService, film: {}", film);
        validate(film);
        filmStorage.update(film);
        return film;
    }

    public void like(int filmId, int userId) {
        log.info("Получен запрос к методу like() класса FilmService, filmId: {}, userId: {}", filmId, userId);
        var film = getById(filmId);
        var user = userService.getById(userId);
        filmStorage.like(film, user);
    }

    public void deleteLike(int filmId, int userId) {
        log.info("Получен запрос к методу deleteLike() класса FilmService, filmId: {}, userId: {}", filmId, userId);
        var film = getById(filmId);
        var user = userService.getById(userId);
        filmStorage.deleteLike(film, user);
    }

    public List<Film> getTopRated(int count) {
        log.info("Получен запрос к методу getTopRated() класса FilmService, count: {}", count);
        return filmStorage.getTopRated(count);
    }

    private void validate(Film film) {
        var validationResult = FilmValidator.validate(film);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getMessage());
        }
    }
}
