package ru.yandex.practicum.filmorate.model;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.CorrectLogin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Valid
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotNull(message = "login must not be null")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "login must not be empty")
    @CorrectLogin
    private String login;

    @NotBlank(message = "name must not be empty")
    private String name;

    /**
     * Дата рождения должна быть моментом, датой или временем в прошлом или настоящем
     */
    @PastOrPresent(message = "Дата рождения должна быть моментом, датой или временем в прошлом или настоящем")
    private LocalDate birthday;

    private List<Integer> friends;

    public void addFriend(Integer id) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(id);
    }

    public List<Integer> getFriendsId() {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        return friends;
    }
}
