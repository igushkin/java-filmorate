package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.*;

@Service
public class FilmService {
    FilmStorage storage;
    UserService userService;
    private static int id;

    static {
        id = 1;
    }

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return this.storage.getAll();
    }

    public Film getById(int id) {
        return this.storage.getAll()
                .stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Film create(Film film) {
        validate(film);

        film.setId(this.id);
        this.id++;
        storage.create(film);
        return film;
    }

    public Film update(Film film) {
        validate(film);
        storage.update(film);
        return film;
    }

    public void like(int filmId, int userId) {
        var film = this.getById(filmId);
        var user = this.userService.getById(userId);
        this.storage.like(film, user);
    }

    public void deleteLike(int filmId, int userId) {
        var film = this.getById(filmId);
        var user = this.userService.getById(userId);
        this.storage.like(film, user);
    }

    public List<Map.Entry<Film, Integer>> getTopRated(int count) {
        return this.storage.getTopRated(count);
    }

    private void validate(Film film) {
        var validationResult = FilmValidator.validate(film);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getMessage());
        }
    }
}