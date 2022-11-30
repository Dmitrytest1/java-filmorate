package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public boolean addFilmGenres(int filmId, Set<Genre> genres) {
        return genreDbStorage.addFilmGenres(filmId, genres);
    }

    public Set<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Set<Genre> getFilmGenres(int filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }
}
