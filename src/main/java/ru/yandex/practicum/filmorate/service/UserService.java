package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.*;

@Service
public class UserService {
    private final UserStorage storage;
    private static int id;

    static {
        id = 1;
    }

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAll() {
        return this.storage.getAll();
    }

    public User getById(int id) {
        Optional<User> value = this.storage.getAll()
                .stream()
                .filter(x -> x.getId() == id)
                .findFirst();

        if (value.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }

        return value.get();
    }

    public User create(User user) {
        validate(user);

        user.setId(this.id);
        this.id++;
        storage.create(user);
        return user;
    }

    public void addFriend(int u1, int u2) {
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        this.storage.beFriends(user1, user2);
    }

    public User update(User user) {
        validate(user);
        storage.update(user);
        return user;
    }

    public void deleteFriend(int u1, int u2) {
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        this.storage.breakFriendship(user1, user2);
    }

    public List<User> getFriends(int u) {
        User user = this.getById(u);
        return this.storage.getFriends(user);
    }

    public List<User> getMutualFriends(int u1, int u2) {
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        return this.storage.getMutualFriends(user1, user2);
    }

    private void validate(User user) {
        var validationResult = UserValidator.validate(user);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getMessage());
        }
    }
}
