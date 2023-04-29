package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Friendship {
    @EqualsAndHashCode.Include
    private final User requester;
    @EqualsAndHashCode.Include
    private final User recipient;
    private boolean accepted;

}
