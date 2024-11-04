package backend.annotation.DTO.impl;

import backend.annotation.DTO.UserRegisterDTOValidation;
import backend.model.DTO.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRegisterDTOValidationImpl implements ConstraintValidator<UserRegisterDTOValidation, UserRegisterDTO> {
    @Override
    public void initialize(UserRegisterDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext constraintValidatorContext) {
        return userRegisterDTO.getUsername() != null && !userRegisterDTO.getUsername().isEmpty() &&
                userRegisterDTO.getPassword() != null && !userRegisterDTO.getPassword().isEmpty() &&
                userRegisterDTO.getGender() != null && userRegisterDTO.getRole() != null;
    }
}
