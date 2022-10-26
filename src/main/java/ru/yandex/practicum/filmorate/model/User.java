package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.CorrectLogin;

import java.time.LocalDate;

@Data
public class User {
    @PositiveOrZero(message = "id can not be negative")
    private int id; // целочисленный идентификатор
    @NotNull
    @Email
    private String email; // электронная почта
    @NotBlank // Аннотированный элемент не должен быть нулевым и должен содержать хотя бы один непробельный символ
    @CorrectLogin
    private String login; // логин пользователя
    private String name; // имя для отображения
    @PastOrPresent // Аннотированный элемент должен быть моментом, датой или временем в прошлом или настоящем
    private LocalDate birthday; // дата рождения
}
