package backend.annotation.DTO.impl;

import backend.annotation.DTO.TeacherProfileUpdateDTOValidation;
import backend.util.GlobalConfig;
import backend.model.DTO.TeacherProfileUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TeacherProfileUpdateDTOValidationImpl implements ConstraintValidator<TeacherProfileUpdateDTOValidation, TeacherProfileUpdateDTO> {
    @Override
    public void initialize(TeacherProfileUpdateDTOValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(TeacherProfileUpdateDTO teacherProfile, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (teacherProfile == null) {
            context.buildConstraintViolationWithTemplate("Teacher profile is not allowed null").addConstraintViolation();
            return false;
        }

        if (teacherProfile.getGender() < 0 || teacherProfile.getGender() > 1) {
            context.buildConstraintViolationWithTemplate("Gender must be either 0 or 1").addConstraintViolation();
            return false;
        }

        if (!teacherProfile.getEmail().matches(GlobalConfig.emailPattern)) {
            context.buildConstraintViolationWithTemplate("Email format is incorrect").addConstraintViolation();
            return false;
        }

        return true;
    }
}
