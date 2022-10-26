package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Component
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }
}
