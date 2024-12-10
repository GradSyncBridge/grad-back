package backend.annotation.DTO.impl;

import backend.annotation.DTO.UserLoginDTOValidation;
import backend.model.DTO.UserLoginDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UserLoginDTOValidationImpl implements ConstraintValidator<UserLoginDTOValidation, UserLoginDTO> {

    @Override
    public void initialize(UserLoginDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserLoginDTO userLoginDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(userLoginDTO.getPassword() == null || userLoginDTO.getUsername() == null
                || userLoginDTO.getPassword().isEmpty() || userLoginDTO.getUsername().isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid user").addConstraintViolation();
            return false;
        }
        return true;
    }

}
