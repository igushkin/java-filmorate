package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAll() {
        String sqlQuery = "select *" +
                "from rating " +
                "order by id";

        return jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToMpa(x, y));
    }

    public Mpa getById(int id) {
        String sqlQuery = "select *" +
                "from rating " +
                "where rating.id = " + id;

        var result = jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToMpa(x, y));

        if (result.size() < 1) {
            throw new NotFoundException("Не тайден рейтинг с id:" + id);
        }

        return result.get(0);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        return new Mpa(id);
    }
}
