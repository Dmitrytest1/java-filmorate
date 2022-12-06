package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBUserStorageTest {
    private final DBUserStorage dbUserStorage;

    @Test
    public void testGetUserById() {
        User user = new User(1,
                "correct.email@mail.ru",
                "correct_login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        dbUserStorage.addUser(user);
        User dbUser = dbUserStorage.getUser(1);
        assertThat(dbUser).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllUsers() {
        User user1 = new User(1,
                "correct.email@mail.ru",
                "correct_login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User user2 = new User(2,
                "correct.email2@mail.ru",
                "correct_login2",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        dbUserStorage.addUser(user1);
        dbUserStorage.addUser(user2);
        Collection<User> dbUsers = dbUserStorage.getAllUsers();
        assertEquals(2, dbUsers.size());
    }

    @Test
    void updateUser() {
        User user = new User(1,
                "correct.email@mail.ru",
                "correct_login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User addedUser = dbUserStorage.addUser(user);
        user.setName("Ivan");
        dbUserStorage.updateUser(user);
        User dbUser = dbUserStorage.getUser(addedUser.getId());
        assertThat(dbUser).hasFieldOrPropertyWithValue("name", "Ivan");
    }

    @Test
    void deleteUser() {
        User user1 = new User(1,
                "correct.email@mail.ru",
                "correct_login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User user2 = new User(2,
                "correct.email2@mail.ru",
                "correct_login2",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User addedUser1 = dbUserStorage.addUser(user1);
        User addedUser2 = dbUserStorage.addUser(user2);
        Collection<User> beforeDelete = dbUserStorage.getAllUsers();
        dbUserStorage.deleteUser(addedUser1);
        Collection<User> afterDelete = dbUserStorage.getAllUsers();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}
