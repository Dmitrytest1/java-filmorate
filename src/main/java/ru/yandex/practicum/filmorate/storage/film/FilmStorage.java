package ru.yandex.practicum.filmorate.storage.film;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@EqualsAndHashCode
@ToString
public class FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
