package ru.yandex.practicum.filmorate.storage.film;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class FilmStorage {
    public List<Film> films = new ArrayList<>();

    public Film create(Film film) {
        films.add(film);
        return film;
    }
}
