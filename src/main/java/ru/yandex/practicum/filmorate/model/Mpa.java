package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {

    enum MpaName {
        G("G"),
        PG("PG"),
        PG_13("PG-13"),
        R("R"),
        NC_17("NC-17");

        private final String value;

        MpaName(String genreName) {
            this.value = genreName;
        }

        String getValue() {
            return this.value;
        }
    }

    private MpaName mpaName;
    private Integer id;

    public Mpa(Integer id) {
        this.setId(id);
    }

    public void setId(Integer id) {
        this.id = id;

        switch (id) {
            case 1:
                this.mpaName = MpaName.G;
                break;
            case 2:
                this.mpaName = MpaName.PG;
                break;
            case 3:
                this.mpaName = MpaName.PG_13;
                break;
            case 4:
                this.mpaName = MpaName.R;
                break;
            case 5:
                this.mpaName = MpaName.NC_17;
                break;
            default:
                throw new IllegalArgumentException("Передано неверное сообщение для поля MPA");
        }
    }
}
