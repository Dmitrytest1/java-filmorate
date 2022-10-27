package ru.yandex.practicum.filmorate.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Component
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getFilms() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createFilm(@Valid @RequestBody User film) {
        return userService.createUser(film);
    }

    @PutMapping
    public User updateFilm(@RequestBody User user) {
        return userService.updateUser(user);
    }
}