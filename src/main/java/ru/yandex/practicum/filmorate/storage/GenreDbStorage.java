package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Interface.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select *" +
                "from genre " +
                "order by id";

        return jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToGenre(x, y));
    }

    @Override
    public Genre getById(int id) {
        String sqlQuery = "select *" +
                "from genre " +
                "where genre.id = " + id;

        var result = jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToGenre(x, y));

        if (result.size() < 1) {
            throw new NotFoundException("Не тайден жанр с id:" + id);
        }

        return result.get(0);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        return new Genre(id);
    }
}
