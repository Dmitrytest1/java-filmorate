package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public boolean addFilmGenres(int filmId, List<Genre> genres) {
        return genreDbStorage.addFilmGenres(filmId, genres);
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public List<Genre> getFilmGenres(int filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }

    public boolean deleteFilmGenres(int filmId) {
        return genreDbStorage.deleteFilmGenres(filmId);
    }
}
