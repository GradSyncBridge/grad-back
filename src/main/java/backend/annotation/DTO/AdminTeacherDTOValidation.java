package backend.annotation.DTO;

import backend.annotation.DTO.impl.AdminTeacherDTOValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdminTeacherDTOValidationImpl.class)
public @interface AdminTeacherDTOValidation {
    String message() default "Invalid teacher profile format";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
