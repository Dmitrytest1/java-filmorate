package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void shouldGetAllFilms() {
        Film film = new Film(1,
                "Film name",
                "Film description",
                LocalDate.now().minusYears(15),
                100,
                new Mpa(1,"G","Нет возрастных ограничений"),
                new ArrayList<>(),
                new ArrayList<>());
        Film film2 = new Film(2,
                "Second film name",
                "Second film description",
                LocalDate.now().minusYears(15),
                100,
                new Mpa(3,"R","Детям до 13 лет просмотр не желателен"),
                new ArrayList<>(),
                new ArrayList<>());
        filmDbStorage.create(film);
        filmDbStorage.create(film2);
        List<Film> dbFilms = filmDbStorage.getAllFilms();
        assertEquals(3, dbFilms.size());
    }

    @Test
    public void shouldGetFilmById() {
        Film film = new Film();
        film.setMpa(new Mpa(1,"mpa", "desc"));
        film.setName("new film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.now().minusYears(10));
        film.setDuration(60);
        filmDbStorage.create(film);
        Film dbFilm = filmDbStorage.getFilmById(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film(1,
                "Film name",
                "Film description",
                LocalDate.now().minusYears(15),
                100,
                new Mpa(1,"G","Нет возрастных ограничений"),
                new ArrayList<>(),
                new ArrayList<>());
        Film myFilm = filmDbStorage.create(film);
        myFilm.setName("update");
        filmDbStorage.update(myFilm);
        Film dbFilm = filmDbStorage.getFilmById(myFilm.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "update");
    }
}
