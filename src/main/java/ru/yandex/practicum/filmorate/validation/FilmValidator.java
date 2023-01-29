package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilmValidator {

    public static ValidationResult validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            return new ValidationResult(false, "название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            return new ValidationResult(false, "максимальная длина описания 200 символов");
        }
        Calendar cal = Calendar.getInstance();
        cal.set(1895, Calendar.DECEMBER, 28); //Year, month and day of month
        Date date = cal.getTime();
        if (film.getReleaseDate() != null && film.getReleaseDate().before(date)) {
            return new ValidationResult(false, "Дата релиза не может быть раньше 28 декабря 1895 г.");
        }
        if (film.getDuration() != null && film.getDuration() < 1) {
            return new ValidationResult(false, "Продолжительность фильма не может быть меньше 1");
        }
        return new ValidationResult(true, null);
    }

}
