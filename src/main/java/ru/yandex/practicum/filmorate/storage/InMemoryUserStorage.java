package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.Storage;
import ru.yandex.practicum.filmorate.storage.Interface.UserStorage;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends UserStorage {

    private HashMap<User, HashSet<User>> friendship;

    public InMemoryUserStorage() {
        this.friendship = new HashMap<>();
    }

    @Override
    public void beFriends(User u1, User u2) {
        if (!friendship.containsKey(u1)) {
            friendship.put(u1, new HashSet<>(Arrays.asList(u2)));
        } else {
            friendship.get(u1).add(u2);
        }

        if (!friendship.containsKey(u2)) {
            friendship.put(u2, new HashSet<>(Arrays.asList(u1)));
        } else {
            friendship.get(u2).add(u1);
        }
    }

    @Override
    public void breakFriendship(User u1, User u2) {
        friendship.get(u1).remove(u2);
        friendship.get(u2).remove(u1);
    }

    @Override
    public List<User> getFriends(User u) {
        return friendship.get(u).stream().collect(Collectors.toList());
    }

    @Override
    public ArrayList<User> getMutualFriends(User u1, User u2) {
        var list1 = friendship.get(u1);
        var list2 = friendship.get(u2);

        if (list1 == null || list2 == null) {
            return new ArrayList<>();
        }

        var mutual = (HashSet) list1.clone();
        mutual.retainAll(list2);

        return new ArrayList<>(mutual);
    }
}
