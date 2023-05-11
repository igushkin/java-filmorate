package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends FilmStorage {
    private final Map<Film, Integer> likes;

    public InMemoryFilmStorage() {
        this.likes = new HashMap<>();
    }

    @Override
    public void like(Film film, User user) {

        if (likes.containsKey(film)) {
            int count = likes.get(film);
            likes.remove(film);
            likes.put(film, count + 1);
        } else {
            likes.put(film, 1);
        }
    }

    @Override
    public void deleteLike(Film film, User user) {
        int count = likes.get(film);
        likes.remove(film);
        if (count == 1) {
            return;
        }
        likes.put(film, count - 1);
    }


    @Override
    public List<Film> getTopRated(int count) {

        var films = this.getAll();
        var rating = new ArrayList<Map.Entry<Film, Integer>>();

        for (var film : films) {
            if (!likes.containsKey(film)) {
                rating.add(Map.entry(film, 0));
            } else {
                rating.add(Map.entry(film, likes.get(film)));
            }
        }

        // Sort the list
        Collections.sort(rating, new Comparator<Map.Entry<Film, Integer>>() {
            public int compare(Map.Entry<Film, Integer> o1,
                               Map.Entry<Film, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        return rating.stream().limit(count).map(x -> x.getKey()).collect(Collectors.toList());
    }

    @Override
    public Film getById(Integer id) {
        var value = storage
                .stream()
                .filter(x -> x.getId() == id)
                .findFirst();

        if (value.isEmpty()) {
            throw new NotFoundException("Такой фильм не найден");
        }

        return value.get();
    }
}
