package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDbStorage extends FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void like(Film film, User user) {
        String sqlQuery = "insert into likes " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                film.getId());

    }

    @Override
    public void deleteLike(Film film, User user) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }


    @Override
    public List<Film> getTopRated(int count) {

        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM (SELECT FILM_ID, count(*) AS likes " +
                        "FROM likes " +
                        "GROUP BY FILM_ID) AS likesAgr " +
                        "RIGHT JOIN FILM ON likesAgr.FILM_ID = FILM.FILM_ID " +
                        "LEFT JOIN RATING ON film.RATING_ID = rating.ID " +
                        "ORDER BY likes DESC " +
                        "LIMIT (" + count + ")", (x, y) -> mapRowToFilm(x, y, getGenres()));
    }

    public void create(Film film) {
        String sqlQuery = "insert into film (film_id, name, rating_id, description, releasedate, duration) " +
                "values (?, ?, ?, ?, ?, ?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate().format(formatter),
                film.getDuration());

        if (film.getGenres() != null && film.getGenres().size() > 0) {
            sqlQuery = "insert into film_to_genre (film_id, genre_id) values ";

            for (var genre : film.getGenres()) {
                sqlQuery += "(" + film.getId() + ", " + genre.getId() + "), ";
            }

            sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 2);

            jdbcTemplate.update(sqlQuery);
        }
    }

    private HashMap<Integer, HashSet<Genre>> getGenres() {
        String sqlQuery = "select * from film_to_genre " +
                "LEFT JOIN genre ON genre.ID  = film_to_genre.GENRE_ID ";

        var list = jdbcTemplate.query(sqlQuery, (x, y) -> {
            return Map.entry(x.getInt(1), x.getString("genre.ID"));
        });

        var map = new HashMap<Integer, HashSet<Genre>>();

        for (var entry : list) {

            var genreId = Integer.parseInt(entry.getValue());

            if (!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), new HashSet<>(List.of(new Genre(genreId))));
            } else {
                map.get(entry.getKey()).add(new Genre(genreId));
            }
        }

        return map;
    }

    public Film update(Film film) {

        String sqlQuery = "update film set " +
                "name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";

        var updated = jdbcTemplate.update(sqlQuery,
                film.getName() == null ? "" : film.getName(),
                film.getDescription() == null ? "" : film.getDescription(),
                film.getReleaseDate() == null ? "" : film.getReleaseDate(),
                film.getDuration() == null ? "" : film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (updated == 0) {
            throw new NotFoundException("Фильм не найден в базе данных");
        }

        sqlQuery = "delete from film_to_genre where film_to_genre.film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        if (film.getGenres() != null && film.getGenres().size() > 0) {
            sqlQuery = "insert into film_to_genre (film_id, genre_id) values ";

            for (var genre : film.getGenres()) {
                sqlQuery += "(" + film.getId() + ", " + genre.getId() + "), ";
            }

            sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 2);

            jdbcTemplate.update(sqlQuery);
        }

        return film;
    }

    public Film remove(Film film) {
        String sqlQuery = "delete from film where id = ?";

        jdbcTemplate.update(sqlQuery, film.getId());

        return film;
    }

    public List<Film> getAll() {
        String sqlQuery = "select * from film " +
                "LEFT join film_to_genre as ftg on ftg.film_id = film.film_id " +
                "LEFT JOIN RATING ON film.RATING_ID = rating.ID " +
                "LEFT JOIN (" +
                "SELECT LIKES.FILM_ID, count(*) AS likes " +
                "FROM LIKES " +
                "GROUP BY LIKES.FILM_ID) AS tb ON TB.FILM_ID = film.FILM_ID";

        return jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToFilm(x, y, getGenres()))
                .stream().collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum, Map<Integer, HashSet<Genre>> genres) throws SQLException {
        int id = rs.getInt("FILM.FILM_ID");
        String name = rs.getString("NAME");
        Mpa mpa = new Mpa(rs.getInt("rating.id"));
        String desc = rs.getString("DESCRIPTION");
        Date date = rs.getDate("RELEASEDATE");
        LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        int duration = rs.getInt("DURATION");
        int likes = rs.getString("LIKES") == null ? 0 : rs.getInt("LIKES");
        var filmGenres = genres.get(id) == null ? new HashSet<Genre>() : genres.get(id);

        return new Film(id, name, filmGenres, likes, mpa, desc, localDate, duration);
    }
}
