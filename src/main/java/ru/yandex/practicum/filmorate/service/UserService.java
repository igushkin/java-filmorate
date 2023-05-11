package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private static int id;

    static {
        id = 1;
    }

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        log.info("Получен запрос к методу getAll() класса UserService");
        return userStorage.getAll();
    }

    public User getById(int id) {
        log.info("Получен запрос к методу getById() класса UserService, id: {}", id);
        return userStorage.getById(id);
    }

    public User create(User user) {
        log.info("Получен запрос к методу create() класса UserService, user: {}", user);
        validate(user);

        user.setId(this.id);
        userStorage.create(user);
        this.id++;
        return user;
    }

    public void addFriend(int u1, int u2) {
        log.info("Получен запрос к методу addFriend() класса UserService, 1userID: {}, 2userID: {}", u1, u2);
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        userStorage.addFriend(user1, user2);
    }

    public User update(User user) {
        log.info("Получен запрос к методу update() класса UserService, user: {}", user);
        validate(user);
        userStorage.update(user);
        return user;
    }

    public void deleteFriend(int u1, int u2) {
        log.info("Получен запрос к методу deleteFriend() класса UserService, 1userID: {}, 2userID: {}", u1, u2);
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        userStorage.breakFriendship(user1, user2);
    }

    public List<User> getFriends(int userId) {
        log.info("Получен запрос к методу getFriends() класса UserService, userID: {}", userId);
        User user = this.getById(userId);
        return userStorage.getFriends(user);
    }

    public List<User> getMutualFriends(int u1, int u2) {
        log.info("Получен запрос к методу getMutualFriends() класса UserService, 1userID: {}, 2userID: {}", u1, u2);
        User user1 = this.getById(u1);
        User user2 = this.getById(u2);

        return userStorage.getMutualFriends(user1, user2);
    }

    private void validate(User user) {
        var validationResult = UserValidator.validate(user);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getMessage());
        }
    }
}
