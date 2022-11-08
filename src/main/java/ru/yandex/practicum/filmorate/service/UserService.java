package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    /**
     * Получение всех пользователей
     */
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    /**
     * Получение пользователя
     */
    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    /**
     * Создание нового пользователя
     */
    public User createUser(User user) {
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    /**
     * Редактирование пользователя
     */
    public User updateUser(User user) {
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    /**
     * Добавление в список друзей
     */
    public void addFriend(Integer userId, Integer friendId) {
        if (getUserById(userId).getFriends() == null) {
            getUserById(userId).addFriend(friendId);
            getUserById(friendId).addFriend(userId);
            log.debug("Пользователь с id {} добавил в список друзей пользователя с id {}", userId, friendId);
        } else {
            if (!(getUserById(userId).getFriends().contains(friendId))) {
                getUserById(userId).addFriend(friendId);
                getUserById(friendId).addFriend(userId);
                log.debug("Пользователь с id {} добавил в список друзей пользователя с id {}", userId, friendId);
            } else throw new CustomValidationException("Пользователи уже состоят в друзьях");
        }
    }

    /**
     * Удаление из списка друзей
     */
    public void deleteFriend(Integer userId, Integer friendId) {
        if (getUserById(userId).getFriends().contains(friendId)) {
            getUserById(userId).getFriends().remove(friendId);
            getUserById(friendId).getFriends().remove(userId);
            log.debug("Пользователь с id {} удален из списка друзей пользователем с id {}", userId, friendId);
        } else throw new CustomValidationException("Пользователи не состоят в друзьях");
    }

    /**
     * Получение всех друзей пользователя
     */
    public List<User> getUserFriends(Integer userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        return userStorage.getUserFriends(userId);
    }

    /**
     * Получение общих друзей с другим пользователем
     */
    public List<User> getMutualFriends(Integer userId, Integer otherId) {
        return getUserById(userId).getFriends().stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .map(this::getUserById).collect(Collectors.toList());
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}

