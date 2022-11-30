package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Autowired(required = false) UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }


    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilmsPopular(Integer count) {
        return filmStorage.getFilmsPopular(count);
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film, "Добавлен");
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film, "Обновлен");
        return filmStorage.update(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        filmStorage.addLike(filmId, userId);
        log.info("like for film with id={} added", filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("like for film with id={} deleted", filmId);
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA)) {
            throw new ValidationException("Дата релиза не может быть раньше " + START_DATA);
        }
        log.debug("{} фильм: {}", text, film.getName());
    }
}
