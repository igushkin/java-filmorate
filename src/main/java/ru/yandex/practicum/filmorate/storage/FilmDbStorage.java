package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?)";
        jdbcTemplate.update(sqlQuery);
    }


    @Override
    public List<Film> getTopRated(int count) {

        return jdbcTemplate.query("SELECT DURATION," +
                "FILM.FILM_ID, " +
                "GENRE, " +
                "NAME, " +
                "RATING_ID, " +
                "RELEASEDATE, " +
                "COUNT(*) " +
                "FROM LIKES " +
                "LEFT JOIN FILM ON LIKES.FILM_ID = FILM.FILM_ID " +
                "GROUP BY FILM.FILM_ID " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT (" + count + ")", (x, y) -> mapRowToFilm(x, y, getGenres())
        ).stream().collect(Collectors.toList());
    }

    public void create(Film film) {
        String sqlQuery = "insert into film (film_id, name, rating_id, description, releasedate, duration) " +
                "values (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getRating(),
                film.getDescription(),
                new SimpleDateFormat("yyyy-MM-dd").format(film.getReleaseDate()),
                film.getDuration());

        sqlQuery = "insert into film_to_genre (film_id, genre_id) ";

        for (var genre : film.getGenreIds()) {
            sqlQuery = "values (" + film.getId() + ", " + genre + 1 + "), ";
        }

        jdbcTemplate.update(sqlQuery);
    }

    private HashMap<Integer, List<Integer>> getGenres() {
        String sqlQuery = "slect * from film_to_genre";

        var list = jdbcTemplate.query(sqlQuery, (x, y) -> {
            return Map.entry(x.getInt(1), x.getInt(2));
        });

        var map = new HashMap<Integer, List<Integer>>();

        for (var entry : list) {

            var genre = entry.getValue();

            if (!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), new ArrayList<>(List.of(genre)));
            } else {
                map.get(entry.getKey()).add(genre);
            }
        }

        return map;
    }

    public Film update(Film film) {

        String sqlQuery = "update film set " +
                "name = ?, genre = ?, rating_id = ?, description = ?, releaseDate = ?, duration = ? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getGenreIds(),
                film.getRating(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        return film;
    }

    public Film remove(Film film) {
        String sqlQuery = "delete from film where id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        return film;
    }

    public List<Film> getAll() {
        String sqlQuery = "select * from film " +
                "right join film_to_genre as ftg on ftg.film_id = film.film_id";

        return jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToFilm(x, y, getGenres()))
                .stream().collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum, Map<Integer, List<Integer>> genres) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        Rating rating = Rating.values()[rs.getInt("RATING_ID") - 1];
        String desc = rs.getString("DESCRIPTION");
        Date rDate = rs.getDate("RELEASEDATE");
        int duration = rs.getInt("DURATION");
        var filmGenres = genres.get(id);

        return new Film(id, name, filmGenres, rating, desc, rDate, duration);
    }
}
