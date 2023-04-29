package ru.yandex.practicum.filmorate.storage.Interface;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface GenreStorage {
    public List<Genre> getAll();

    public Genre getById(int id);
}
