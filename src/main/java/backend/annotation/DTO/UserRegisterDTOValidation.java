package backend.annotation.DTO;

import backend.annotation.DTO.impl.UserRegisterDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRegisterDTOValidationImpl.class)
public @interface UserRegisterDTOValidation {

    String message() default "Invalid User";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
