package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addFilmGenres(Integer filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            String setNewGenres = "insert into FILMGENRE (FilmID, GenreID) values (?, ?) ON CONFLICT DO NOTHING";
            jdbcTemplate.update(setNewGenres, filmId, genre.getId());
        }
        return true;
    }

    public Set<Genre> getAllGenres() {
        String sqlGenre = "select GenreID, Name from GENRE ORDER BY GenreID";
        return (Set<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    public Set<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRE.GenreID, Name from Genre " +
                "INNER JOIN FILMGENRE FG on GENRE.GenreID = Fg.GenreID " +
                "where FilmID = ?";
        return (Set<Genre>) jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre(rs.getInt("GenreID"), rs.getString("Name"));
        return genre;
    }
}
