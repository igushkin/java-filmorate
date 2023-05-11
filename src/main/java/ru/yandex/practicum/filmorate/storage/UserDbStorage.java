package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDbStorage extends UserStorage {

    @Override
    public User getById(Integer id) {
        String sqlQuery = "select * from my_user " +
                "WHERE MY_USER.USER_ID = " + id;

        var queryResult = jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToUser(x, y))
                .stream()
                .collect(Collectors.toList());
        if (queryResult.size() == 0) {
            throw new NotFoundException("Такой пользователь не найден");
        } else {
            return queryResult.get(0);
        }
    }

    @Override
    public void addFriend(User requester, User recipient) {
        String sqlQuery = "insert into FRIENDSHIP " +
                "(requester, recipient) " +
                "values (?, ?)";

        jdbcTemplate.update(sqlQuery,
                requester.getId(),
                recipient.getId()
        );
    }

    @Override
    public void breakFriendship(User u1, User u2) {
        String sqlQuery = "delete from FRIENDSHIP " +
                "where REQUESTER = ? and RECIPIENT = ?";

        jdbcTemplate.update(sqlQuery, u1.getId(), u2.getId());
    }

    @Override
    public List<User> getFriends(User u) {
        String sqlQuery = "SELECT * " +
                "FROM MY_USER mu " +
                "WHERE mu.USER_ID in " +
                "(SELECT RECIPIENT " +
                "FROM FRIENDSHIP " +
                "WHERE REQUESTER = " + u.getId() + " )";

        return jdbcTemplate.query(sqlQuery,
                        (x, y) -> mapRowToUser(x, y))
                .stream().collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(User u1, User u2) {
        String sqlQuery = "SELECT * " +
                "FROM MY_USER " +
                "WHERE MY_USER.USER_ID IN( " +
                "SELECT tmp.RECIPIENT " +
                "FROM " +
                "(SELECT f.RECIPIENT , count(*) AS num " +
                "FROM FRIENDSHIP AS f " +
                "WHERE f.REQUESTER = " + u1.getId() + " OR f.REQUESTER = " + u2.getId() + " " +
                "GROUP BY f.RECIPIENT " +
                "HAVING num > 1) AS tmp " +
                ")";

        return jdbcTemplate.query(sqlQuery,
                        (x, y) -> mapRowToUser(x, y))
                .stream().collect(Collectors.toList());
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(User obj) {
        String sqlQuery = "insert into MY_USER " +
                "(user_id, email, login, name, birthday) " +
                "values (?, ?, ?, ?, ?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        jdbcTemplate.update(sqlQuery,
                obj.getId(),
                obj.getEmail(),
                obj.getLogin(),
                obj.getName(),
                obj.getBirthday().format(formatter));
    }

    @Override
    public User update(User obj) {
        String sqlQuery = "update MY_USER set " +
                "email = ?, login = ?, name = ?, birthday = ?" +
                "where user_id = ?";

        var updated = jdbcTemplate.update(sqlQuery,
                obj.getEmail() == null ? "" : obj.getEmail(),
                obj.getLogin() == null ? "" : obj.getLogin(),
                obj.getName() == null ? "" : obj.getName(),
                obj.getBirthday() == null ? "" : obj.getBirthday(),
                obj.getId());

        if (updated == 0) {
            throw new NotFoundException("Пользователь не найден в базе данных");
        }

        return obj;
    }

    @Override
    public User remove(User obj) {
        String sqlQuery = "delete from MY_USER where id = ?";

        jdbcTemplate.update(sqlQuery, obj.getId());

        return obj;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from my_user ";

        return jdbcTemplate.query(sqlQuery, (x, y) -> mapRowToUser(x, y))
                .stream()
                .collect(Collectors.toList());
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("my_user.user_ID");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        Date date = rs.getDate("birthday");
        LocalDate birthday = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());

        return new User(id, email, login, name, birthday);
    }
}