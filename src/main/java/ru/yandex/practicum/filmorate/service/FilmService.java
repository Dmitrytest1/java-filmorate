package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }
}
