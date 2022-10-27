package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.annotations.CorrectReleaseDay;

@Data
@AllArgsConstructor
public class Film {
    @PositiveOrZero(message = "id can not be negative")
    private int id; // целочисленный идентификатор
    @NotBlank(message = "name must not be empty")
    private String name; // название фильма
    @Length(min = 1, max = 200, message = "description length must be between 1 and 200")
    private String description; // описание
    @CorrectReleaseDay(message = "releaseDate must be after 28-DEC-1895")
    private LocalDate releaseDate; // дата релиза
    @PositiveOrZero(message = "duration can not be negative")
    private Integer duration; // продолжительность фильма
}
