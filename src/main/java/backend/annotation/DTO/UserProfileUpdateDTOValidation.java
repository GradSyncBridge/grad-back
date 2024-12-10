package backend.annotation.DTO;

import backend.annotation.DTO.impl.UserProfileUpdateDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserProfileUpdateDTOValidationImpl.class)
public @interface UserProfileUpdateDTOValidation {
    String message() default "Invalid user";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
