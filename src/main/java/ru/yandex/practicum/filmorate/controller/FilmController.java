package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmsList= new ArrayList<>(filmService.getAllFilms().values());
        log.debug("Количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (filmService.getAllFilms().containsKey(film.getId())) {
            throw new RuntimeException("Фильм уже есть в базе");
        }
        validate(film, "Добавлен");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!filmService.getAllFilms().containsKey(film.getId())) {
            throw new RuntimeException("Фильм нет в базе");
        }
        validate(film, "Обновлен");
        return filmService.updateFilm(film);
    }

    void validate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA))
            throw new ValidationException("Дата релиза не может быть раньше " + START_DATA);
        log.debug("{} фильм: {}", text, film.getName());
    }
}
