package ru.yandex.practicum.filmorate.storage.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@EqualsAndHashCode
@ToString
public class UserStorage {
    public List<User> users = new ArrayList<>();

    public User create(User user) {
        users.add(user);
        return user;
    }

    public User update(User user) {
        users.set(user.getId(), user);
        return user;
    }
}
