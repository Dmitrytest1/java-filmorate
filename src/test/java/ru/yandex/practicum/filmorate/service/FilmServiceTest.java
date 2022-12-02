package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FilmServiceTest {
    @Autowired
    FilmService service;

    private Film film;

    @BeforeEach
    public void beforeEach() {
        film = new Film(1,
                "Film name",
                "Film description",
                LocalDate.now().minusYears(15),
                100,
                new Mpa(1,"G","Нет возрастных ограничений"),
                new ArrayList<>(),
                new ArrayList<>());
    }

    @Test
    void shouldAddWhenAddValidFilmData() {
        Film addedFilm = service.createFilm(film);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Film description");
        film.setReleaseDate(LocalDate.of(2022,12,2));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G", "Нет возрастных ограничений"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.createFilm(film));
        assertEquals("Ошибка валидации Фильма: " + "name must not be empty", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameBlank() {
        Film film = new Film();
        film.setName("  ");
        film.setDescription("Film description");
        film.setReleaseDate(LocalDate.of(2022,12,2));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G", "Нет возрастных ограничений"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.createFilm(film));
        assertEquals("Ошибка валидации Фильма: " + "name must not be empty", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedFilmId() {
        Film film = new Film();
        film.setId(999);
        film.setName("Film name");
        film.setDescription("Film description");
        film.setReleaseDate(LocalDate.of(2022,12,2));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G", "Нет возрастных ограничений"));
        FilmNotFoundException ex = assertThrows(FilmNotFoundException.class, () -> service.updateFilm(film));
        assertEquals("Фильма с id=999 нет в базе", ex.getMessage());
    }
}
