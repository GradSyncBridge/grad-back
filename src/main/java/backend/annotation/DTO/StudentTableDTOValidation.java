package backend.annotation.DTO;

import backend.annotation.DTO.impl.StudentTableDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentTableDTOValidationImpl.class)
public @interface StudentTableDTOValidation {

    String message() default "Invalid Student";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
