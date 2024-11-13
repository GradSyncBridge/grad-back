package backend.annotation.DTO;

import backend.annotation.DTO.impl.StudentSubmitDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentSubmitDTOValidationImpl.class)
public @interface StudentSubmitDTOValidation {
    String message() default "Invalid Student";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
