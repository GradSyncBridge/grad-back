package backend.annotation.DTO.impl;

import backend.annotation.DTO.UserProfileUpdateDTOValidation;
import backend.model.DTO.UserProfileUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileUpdateDTOValidationImpl implements ConstraintValidator<UserProfileUpdateDTOValidation, UserProfileUpdateDTO> {
    @Override
    public void initialize(UserProfileUpdateDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserProfileUpdateDTO userProfileUpdateDTO, ConstraintValidatorContext constraintValidatorContext) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(userProfileUpdateDTO.getEmail());

        return userProfileUpdateDTO.getUsername() != null &&
                !userProfileUpdateDTO.getUsername().isEmpty() &&
                !specialCharPattern.matcher(userProfileUpdateDTO.getUsername()).find() &&
                userProfileUpdateDTO.getName() != null &&
                !userProfileUpdateDTO.getName().isEmpty() &&
                matcher.matches() &&
                userProfileUpdateDTO.getGender() != null &&
                userProfileUpdateDTO.getGender() >= 0 &&
                userProfileUpdateDTO.getGender() <= 2 &&
                userProfileUpdateDTO.getPhone() != null &&
                !userProfileUpdateDTO.getPhone().isEmpty();
    }
}
