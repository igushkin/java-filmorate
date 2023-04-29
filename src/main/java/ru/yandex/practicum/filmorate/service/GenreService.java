package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Interface.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage storage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(int id) {
        return storage.getById(id);
    }
}
