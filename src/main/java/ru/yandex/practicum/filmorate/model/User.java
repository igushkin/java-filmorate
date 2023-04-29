package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank
    @Pattern(regexp = ".*@.*")
    private String email;
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
