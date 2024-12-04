package backend.annotation.DTO;

import backend.annotation.DTO.impl.NoticeCreateValidationImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoticeCreateValidationImpl.class)
public @interface NoticeCreateValidation {

    String message() default "Invalid User";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
