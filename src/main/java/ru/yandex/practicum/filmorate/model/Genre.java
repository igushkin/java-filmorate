package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Genre {

    enum GenreName {
        Comedy("Комедия"),
        Drama("Драма"),
        Cartoon("Мультфильм"),
        Thriller("Триллер"),
        Documentary("Документальный"),
        Action("Боевик");

        private final String value;

        GenreName(String genreName) {
            this.value = genreName;
        }

        String getValue() {
            return this.value;
        }
    }

    private int id;
    private GenreName genreName;

    public Genre(Integer id) {
        setId(id);
    }

    public void setId(Integer id) {
        this.id = id;

        switch (id) {
            case 1:
                this.genreName = GenreName.Comedy;
                break;
            case 2:
                this.genreName = GenreName.Drama;
                break;
            case 3:
                this.genreName = GenreName.Cartoon;
                break;
            case 4:
                this.genreName = GenreName.Thriller;
                break;
            case 5:
                this.genreName = GenreName.Documentary;
                break;
            case 6:
                this.genreName = GenreName.Action;
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
