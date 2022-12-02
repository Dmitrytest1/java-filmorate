package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ru.yandex.practicum.filmorate.annotations.CorrectReleaseDay;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotBlank(message = "name must not be empty")
    private String name;

    @Length(max = 200, message = "description length must be between 1 and 200")
    private String description;

    @CorrectReleaseDay(message = "releaseDate must be after 28-DEC-1895")
    private LocalDate releaseDate;

    @PositiveOrZero(message = "duration can not be negative")
    private Integer duration;

    @NotNull
    private Mpa mpa;

    private List<Genre> genres = new ArrayList<>();
    private List<Integer> likes = new ArrayList<>();

    public void addLike(Integer id) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
}
