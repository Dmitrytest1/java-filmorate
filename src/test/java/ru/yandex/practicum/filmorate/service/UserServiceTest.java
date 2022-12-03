package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    private User user;

    @Autowired
    UserService service;

    @BeforeEach
    public void beforeEach() {
        user = new User(0,
                "test@mail.ru",
                "test_login",
                "Test Name",
                LocalDate.of(2022, 12, 3),
                new ArrayList<>());
    }

    @Test
    void shouldValidUser() {
        User addedUser = service.createUser(user);
        assertNotEquals(0, addedUser.getId());
        assertTrue(service.getAllUsers().contains(addedUser));
    }

    @Test
    void shouldSetUserNameWhenEmptyUserName() {
        User user2 = new User(0,
                "test2@mail.ru",
                "test_login2",
                "",
                LocalDate.of(2022, 12, 3),
                new ArrayList<>());
        User addedUser = service.createUser(user2);
        assertNotEquals(0, addedUser.getId());
        assertEquals(addedUser.getLogin(), addedUser.getName());
        assertTrue(service.getAllUsers().contains(addedUser));
    }

    @Test
    void shouldThrowExceptionWhenFailedUserLogin() {
        User user3 = new User(0,
                "test3@mail.ru",
                "incorrect login",
                "Test Name",
                LocalDate.of(2022, 12, 3),
                new ArrayList<>());
        UserValidationException ex = assertThrows(UserValidationException.class, () -> service.createUser(user3));
        assertEquals("Ошибка валидации Пользователя: " +
                "must not have space", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFailedUserEmail() {
        User user4 = new User(0,
                "test4@",
                "test_login",
                "Test Name",
                LocalDate.of(2022, 12, 3),
                new ArrayList<>());
        UserValidationException ex = assertThrows(UserValidationException.class, () -> service.createUser(user4));
        assertEquals("Ошибка валидации Пользователя: " +
                "invalid email", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFailedUserBirthDate() {
        User user5 = new User(0,
                "test5@mail.ru",
                "test_login",
                "Test Name",
                LocalDate.now().plusDays(1),
                new ArrayList<>());
        UserValidationException ex = assertThrows(UserValidationException.class, () -> service.createUser(user5));
        assertEquals("Ошибка валидации Пользователя: " +
                "Дата рождения должна быть моментом, датой или временем в прошлом или настоящем", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedUserId() {
        User user6 = new User();
        user6.setId(999);
        user6.setEmail("test6@mail.ru");
        user6.setLogin("test_login");
        user6.setName("Test Name");
        user6.setBirthday(LocalDate.of(2022, 12, 3));
        user6.setFriends(new ArrayList<>());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.updateUser(user6));
        assertEquals("Пользователь с идентификатором 999 не зарегистрирован!", ex.getMessage());
    }
}
