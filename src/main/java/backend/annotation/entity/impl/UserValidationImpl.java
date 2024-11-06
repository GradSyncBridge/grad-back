package backend.annotation.entity.impl;

import backend.annotation.entity.UserValidation;
import backend.annotation.entity.group.userGroup.EmailGroup;
import backend.model.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidationImpl implements ConstraintValidator<UserValidation, User> {

    Class<?> group;

    @Override
    public void initialize(UserValidation constraintAnnotation) {
        group = constraintAnnotation.groups()[0];

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {

        if(group.equals(EmailGroup.class)) {
            String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getEmail());

            if (!matcher.matches()) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid email format").addConstraintViolation();
                return false;
            }
            return true;
        }

        return false;
    }
}
