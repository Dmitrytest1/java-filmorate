package ru.yandex.practicum.filmorate.storage.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
@EqualsAndHashCode
@ToString
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserFoundException(String.format("Пользователь с id=%d есть в базе", user.getId()));
        }
        int newTaskId = generateId();
        user.setId(newTaskId);
        users.put(newTaskId, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("Пользователя с id=%d нет в базе", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>(users.values());
        log.debug("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @Override
    public User getUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
        return users.get(id);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        return users.get(userId).getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }
}
