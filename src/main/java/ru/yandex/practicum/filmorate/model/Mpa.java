package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {

    private static final String G = "G";
    private static final String PG = "PG";
    private static final String PG_13 = "PG-13";
    private static final String R = "R";
    private static final String NC_17 = "NC-17";
    private String name;
    private Integer id;
    
    public Mpa(Integer id) {
        this.setId(id);
    }

    public void setId(Integer id) {
        this.id = id;

        switch (id) {
            case 1:
                this.name = G;
                break;
            case 2:
                this.name = PG;
                break;
            case 3:
                this.name = PG_13;
                break;
            case 4:
                this.name = R;
                break;
            case 5:
                this.name = NC_17;
                break;
            default:
                throw new IllegalArgumentException("Передано неверное сообщение для поля MPA");
        }
    }
}
