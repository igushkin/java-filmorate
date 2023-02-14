package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Interface.Storage;

import java.util.*;

@Component
public class InMemoryFilmStorage extends FilmStorage {
    private Map<Film, Integer> likes;

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
    public List<Map.Entry<Film, Integer>> getTopRated(int count) {

        // Create a list from elements of HashMap
        List<Map.Entry<Film, Integer>> list =
                new ArrayList<>(this.likes.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Film, Integer>>() {
            public int compare(Map.Entry<Film, Integer> o1,
                               Map.Entry<Film, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        return list;
    }
}
