package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component("FilmDbStorage")
public class FilmDbStorage  implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILM " +
                "(Name, Description, ReleaseDate, Duration, MpaID) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, Math.toIntExact(film.getMpa().getId()));
            return ps;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilmById(id);
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sqlFilm = "select * from FILM " +
                "INNER JOIN MPA R on FILM.MpaID = R.MpaID " +
                "where FilmID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильма с id=%d нет в базе", filmId));

        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public List<Film> getFilmsPopular(Integer count) {
        return null;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("FilmID");
        Film film = new Film(
                filmId,
                rs.getString("Name"),
                rs.getString("Description"),
                Objects.requireNonNull(rs.getDate("ReleaseDate")).toLocalDate(),
                rs.getInt("Duration"),
                new Mpa(rs.getInt("Mpa.MpaID"),
                        rs.getString("Mpa.Name")),
                (Set<Genre>) genreService.getFilmGenres(filmId),
                (Set<Integer>) getFilmLikes(filmId)
        );
        return film;
    }

    private List<Integer> getFilmLikes(Integer filmId) {
        String sqlGetLikes = "select UserID from Likes where FilmID = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
        return likes;
    }
}
