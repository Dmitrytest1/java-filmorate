package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validator.FilmReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// @Target - параметр, указывающий, для каких видов аннтоцаия может быть применена
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME) // @Retention указывает, в каком жизненном цикле кода наша аннотация будет доступна
@Documented // Аннотация будет помещена в сгенерированную документацию javadoc
@Constraint(validatedBy = FilmReleaseDateValidator.class) // аннотация ограничения
public @interface CorrectReleaseDay {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

