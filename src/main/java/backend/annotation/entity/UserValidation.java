package backend.annotation.entity;

import backend.annotation.entity.group.repeatable.UserValidations;
import backend.annotation.entity.impl.UserValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidationImpl.class)
@Repeatable(UserValidations.class)
public @interface UserValidation {
    String message() default "Invalid user";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
