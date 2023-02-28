package ru.yandex.practicum.filmorate.storage.Interface;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.List;

@Component
public abstract class Storage<T> {

    private final HashSet<T> storage;

    public Storage() {
        this.storage = new HashSet<>();
    }

    public void create(T obj) {
        if (storage.contains(obj)) {
            throw new BadRequestException("Storage exception. Element already exist");
        }
        storage.add(obj);
    }

    public T update(T obj) {
        if (!storage.contains(obj)) {
            throw new NotFoundException("Storage exception. No such element in database");
        }
        storage.remove(obj);
        storage.add(obj);
        return obj;
    }

    public T remove(T obj) {
        storage.remove(obj);
        return obj;
    }

    public List<T> getAll() {
        return storage.stream().collect(Collectors.toList());
    }
}
