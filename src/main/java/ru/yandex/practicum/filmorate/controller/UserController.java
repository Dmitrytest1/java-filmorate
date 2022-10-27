package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getFilms() {
        List<User> usersList = new ArrayList<>(userService.getAllUsers().values());
        log.debug("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User createFilm(@Valid @RequestBody User user) {
        if (userService.getAllUsers().containsKey(user.getId())) {
            throw new RuntimeException("Пользователь уже есть в базе");
        }
        validate(user, "Добавлен");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateFilm(@RequestBody User user) {
        if (!userService.getAllUsers().containsKey(user.getId())) {
            throw new RuntimeException("Пользователя нет в базе");
        }
        validate(user, "Обновлен");
        return userService.updateUser(user);
    }

    void validate(User user, String text) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}