package backend.annotation.DTO.impl;

import backend.annotation.DTO.UserRegisterDTOValidation;
import backend.model.DTO.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UserRegisterDTOValidationImpl implements ConstraintValidator<UserRegisterDTOValidation, UserRegisterDTO> {
    @Override
    public void initialize(UserRegisterDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext constraintValidatorContext) {
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");

        return userRegisterDTO.getUsername() != null && !userRegisterDTO.getUsername().isEmpty() && !specialCharPattern.matcher(userRegisterDTO.getUsername()).find() &&
                userRegisterDTO.getPassword() != null && !userRegisterDTO.getPassword().isEmpty() &&
                userRegisterDTO.getGender() != null && userRegisterDTO.getGender() >= 0 && userRegisterDTO.getGender() <= 2 &&
                userRegisterDTO.getRole() != null;
    }
}
