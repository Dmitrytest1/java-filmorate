package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotBlank(message = "name must not be empty")
    @NotNull(message = "name must not be null")
    private String name;

    @Length(max = 200, message = "description length must be between 1 and 200")
    private String description;
}
