package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addFilmGenres(Integer filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            String setNewGenres = "insert into FILMGENRE (FilmID, GenreID) values (?, ?) ON CONFLICT DO NOTHING";
            jdbcTemplate.update(setNewGenres, filmId, genre.getId());
        }
        return true;
    }

    public List<Genre> getAllGenres() {
        String sqlGenre = "select GenreID, Name from GENRE ORDER BY GenreID";
        return jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRE.GenreID, Name from Genre " +
                "INNER JOIN FILMGENRE FG on GENRE.GenreID = Fg.GenreID " +
                "where FilmID = ?";
        return jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from GENRE where GenreID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " + genreId + " не зарегистрирован!");
        }
        return genre;
    }

    public boolean deleteFilmGenres(int filmId) {
        String deleteOldGenres = "delete from FILMGENRE where FilmID = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
        return true;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre(rs.getInt("GenreID"), rs.getString("Name"));
        return genre;
    }
}
