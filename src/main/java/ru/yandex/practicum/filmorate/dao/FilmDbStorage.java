package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
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
                "(Name, Description, ReleaseDate, Duration, RatingID) " +
                "values (?, ?, ?, ?, ?)";
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
        String sqlQuery = "update FILM " +
                "set Name = ?, Description = ?, ReleaseDate = ?, Duration = ?, RatingID = ? " +
                "where FilmID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreService.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }

        if(film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM " +
                "INNER JOIN RatingMPA R on FILM.RatingID = R.RatingID ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sqlFilm = "select * from FILM " +
                "INNER JOIN RatingMPA R on FILM.RatingID = R.RatingID " +
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
        String sqlMostPopular = "select count(L.LikeID) as likeRate" +
                ",FILM.FilmID" +
                ",FILM.Name, FILM.Description, ReleaseDate, Duration, R.RatingID, R.Name, R.Description from FILM " +
                "left join LIKES L on L.FilmID = FILM.FilmID " +
                "inner join RATINGMPA R on R.RATINGID = FILM.RatingID " +
                "group by FILM.FilmID " +
                "ORDER BY likeRate desc " +
                "limit ?";
        List<Film> films = jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> makeFilm(rs), count);
        return films;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "select * from LIKES where UserID = ? and FilmID = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into LIKES (UserID, FilmID) values (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(rs.next()));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String deleteLike = "delete from LIKES where FilmID = ? and UserID = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("FilmID");
        Film film = new Film(
                filmId,
                rs.getString("Name"),
                rs.getString("Description"),
                Objects.requireNonNull(rs.getDate("ReleaseDate")).toLocalDate(),
                rs.getInt("Duration"),
                new Mpa(rs.getInt("RatingMPA.RatingID"),
                        rs.getString("RatingMPA.Name"),
                        rs.getString("RatingMPA.Description")),
                genreService.getFilmGenres(filmId),
                getFilmLikes(filmId)
        );
        return film;
    }

    private List<Integer> getFilmLikes(Integer filmId) {
        String sqlGetLikes = "select UserID from Likes where FilmID = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
        return likes;
    }
}
