package ru.yandex.practicum.filmorate.storage.film;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
@EqualsAndHashCode
@ToString
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            throw new RuntimeException("Фильм уже есть в базе");
        }
        int newTaskId = generateId();
        film.setId(newTaskId);
        films.put(newTaskId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Фильм нет в базе");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.debug("Количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @Override
    public List<Film> getFilmsPopular(Integer count) {
        return films.values()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        films.get(filmId).addLike(userId);
        log.debug("like for film with id={} added", filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("id", String.format("user with id=%d not found", userId));
        }
        film.deleteLike(userId);
        log.debug("like for film with id={} deleted", filmId);
    }
}
