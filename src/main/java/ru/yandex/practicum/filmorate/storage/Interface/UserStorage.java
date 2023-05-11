package ru.yandex.practicum.filmorate.storage.Interface;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public abstract class UserStorage extends Storage<User> {
    public abstract void addFriend(User u1, User u2);

    public abstract void breakFriendship(User u1, User u2);

    public abstract List<User> getFriends(User u);

    public abstract List<User> getMutualFriends(User u1, User u2);
}
