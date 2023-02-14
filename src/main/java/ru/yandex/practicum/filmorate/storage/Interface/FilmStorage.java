package ru.yandex.practicum.filmorate.storage.Interface;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Component
public abstract class FilmStorage extends Storage<Film> {
    public abstract void like(Film film, User user);

    public abstract void deleteLike(Film film, User user);

    public abstract List<Map.Entry<Film, Integer>> getTopRated(int count);
}
