package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Film {
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank
    private String name;
    private HashSet<Genre> genres;
    private int likes;
    @NotBlank
    private Mpa mpa;
    @Max(200)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
}

