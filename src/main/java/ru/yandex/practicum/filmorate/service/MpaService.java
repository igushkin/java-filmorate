package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Interface.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage storage;

    @Autowired
    public MpaService(@Qualifier("mpaDbStorage") MpaStorage storage) {
        this.storage = storage;
    }

    public List<Mpa> getAll() {
        return storage.getAll();
    }

    public Mpa getById(int id) {
        return storage.getById(id);
    }
}
