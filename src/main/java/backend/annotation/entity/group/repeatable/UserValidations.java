package backend.annotation.entity.group.repeatable;

import backend.annotation.entity.UserValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserValidations {
    UserValidation[] value();
}
