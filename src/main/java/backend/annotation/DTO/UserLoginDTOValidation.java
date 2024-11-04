package backend.annotation.DTO;

import backend.annotation.DTO.impl.UserLoginDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserLoginDTOValidationImpl.class)
public @interface UserLoginDTOValidation {

    String message() default "Invalid User";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
