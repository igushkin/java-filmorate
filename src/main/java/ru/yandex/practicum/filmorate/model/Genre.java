package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {

    private static final String Comedy = "Комедия";
    private static final String Drama = "Драма";
    private static final String Cartoon = "Мультфильм";
    private static final String Thriller = "Триллер";
    private static final String Documentary = "Документальный";
    private static final String Action = "Боевик";

    private int id;
    private String name;

    public Genre() {

    }

    public Genre(Integer id) {
        setId(id);
    }

    public void setId(Integer id) {
        this.id = id;

        switch (id) {
            case 1:
                this.name = Comedy;
                break;
            case 2:
                this.name = Drama;
                break;
            case 3:
                this.name = Cartoon;
                break;
            case 4:
                this.name = Thriller;
                break;
            case 5:
                this.name = Documentary;
                break;
            case 6:
                this.name = Action;
                break;
            default:
                throw new IllegalArgumentException("Передано неверное сообщение для поля MPA");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
