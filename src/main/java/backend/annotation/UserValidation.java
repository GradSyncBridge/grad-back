package backend.annotation;

import backend.annotation.group.repeatable.UserValidations;
import backend.annotation.impl.UserValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidationImpl.class)
@Repeatable(UserValidations.class)
public @interface UserValidation {
    String message() default "Invalid User";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
