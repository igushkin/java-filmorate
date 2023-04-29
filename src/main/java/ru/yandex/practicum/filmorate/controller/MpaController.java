package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {

    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mpa> getAll() {
        return this.service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }
}
