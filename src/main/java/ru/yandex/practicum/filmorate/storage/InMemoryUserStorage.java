package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Interface.UserStorage;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends UserStorage {

    private final HashMap<User, List<Friendship>> friends;

    public InMemoryUserStorage() {
        this.friends = new HashMap<>();
    }

    @Override
    public void beFriends(User u1, User u2) {
        var friendship = new Friendship(u1, u2);
        if (!friends.containsKey(u1)) {
            var list = new ArrayList<>(List.of(friendship));
            friends.put(u1, list);
        } else {
            friends.get(u1).add(friendship);
        }

        if (!friends.containsKey(u2)) {
            var list = new ArrayList<>(List.of(friendship));
            friends.put(u2, list);
        } else {
            friends.get(u2).add(friendship);
        }
    }

    @Override
    public void breakFriendship(User u1, User u2) {
        var friendship = new Friendship(u1, u2);
        friends.get(u1).remove(friendship);
        friends.get(u2).remove(friendship);
    }

    @Override
    public List<User> getFriends(User u) {
        return friends.get(u).stream()
                .map(x -> {
                    if (x.getRecipient() == u) {
                        return x.getRequester();
                    } else {
                        return x.getRecipient();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<User> getMutualFriends(User u1, User u2) {
        var list1 = new ArrayList<>(this.getFriends(u1));
        var list2 = new ArrayList<>(this.getFriends(u2));

        if (list1 == null || list2 == null) {
            return new ArrayList<>();
        }

        var mutual = (HashSet) (list1.clone());
        mutual.retainAll(list2);

        return new ArrayList<>(mutual);
    }
}
