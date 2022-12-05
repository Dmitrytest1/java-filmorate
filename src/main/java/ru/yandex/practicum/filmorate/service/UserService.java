package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private int increment = 0;
    private final Validator validator;
    private final UserStorage userStorage;

    @Autowired
    public UserService(Validator validator, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

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
        validate(user);
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    /**
     * Редактирование пользователя
     */
    public User updateUser(User user) {
        validate(user);
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    /**
     * Добавление в список друзей
     */
    public void addFriend(String userId, String friendId) {
        User user = getStoredUser(userId);
        User friend = getStoredUser(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
    }

    /**
     * Удаление из списка друзей
     */
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.debug("Пользователь с id {} удален из списка друзей пользователем с id {}", userId, friendId);
    }

    /**
     * Получение всех друзей пользователя
     */
    public List<User> getUserFriends(final String userId) {
        User user = getStoredUser(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUserById(id));
        }
        return friends;
    }

    /**
     * Получение общих друзей с другим пользователем
     */
    public Set<User> getMutualFriends(Integer userId, Integer otherId) {
        return getUserById(userId).getFriendsId()
                .stream()
                .filter(getUserById(otherId).getFriendsId()::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }

    private void validate(final User user) {
        if(user.getName() == null) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        }else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не содержит буквенных символов. " +
                    "Установлено значение {} из поля login", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации Пользователя: " + messageBuilder, violations);
        }
        if (user.getId() == 0) {
            user.setId(++increment);
        }
    }

    private Integer idFromString(final String supposedId) {
        try {
            return Integer.valueOf(supposedId);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private User getStoredUser(final String supposedId) {
        final int userId = idFromString(supposedId);
        if (userId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор пользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован!");
        }
        return user;
    }
}

