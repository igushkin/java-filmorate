package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return this.service.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer id) {
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return service.getMutualFriends(id, otherId);
    }

    @PostMapping
    public User create(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.service.create(user);
        return user;
    }

    @PutMapping
    public User update(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.service.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(HttpServletRequest request, @PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        this.service.deleteFriend(id, friendId);
    }
}
