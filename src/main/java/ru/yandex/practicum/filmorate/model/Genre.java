package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Genre {

    private static final String COMEDY = "Комедия";
    private static final String DRAMA = "Драма";
    private static final String CARTOON = "Мультфильм";
    private static final String THRILLER = "Триллер";
    private static final String DOCUMENTARY = "Документальный";
    private static final String ACTION = "Боевик";

    private int id;
    private String name;

    public Genre(Integer id) {
        setId(id);
    }

    public void setId(Integer id) {
        this.id = id;

        switch (id) {
            case 1:
                this.name = COMEDY;
                break;
            case 2:
                this.name = DRAMA;
                break;
            case 3:
                this.name = CARTOON;
                break;
            case 4:
                this.name = THRILLER;
                break;
            case 5:
                this.name = DOCUMENTARY;
                break;
            case 6:
                this.name = ACTION;
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
