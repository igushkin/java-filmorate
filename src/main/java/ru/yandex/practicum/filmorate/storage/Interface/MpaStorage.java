package ru.yandex.practicum.filmorate.storage.Interface;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    public List<Mpa> getAll();

    public Mpa getById(int id);
}
