package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        userService = service;
    }

    @GetMapping
    public List<User> getAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(HttpServletRequest request, @PathVariable("id") Integer id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(HttpServletRequest request, @PathVariable("id") Integer id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    public User create(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.userService.create(user);
        return user;
    }

    @PutMapping
    public User update(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.userService.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.userService.deleteFriend(id, friendId);
    }
}
