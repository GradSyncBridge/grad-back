package backend.annotation.DTO.impl;

import backend.annotation.DTO.TeacherProfileUpdateDTOValidation;
import backend.model.DTO.TeacherProfileUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TeacherProfileUpdateDTOValidationImpl implements ConstraintValidator<TeacherProfileUpdateDTOValidation, TeacherProfileUpdateDTO> {
    @Override
    public void initialize(TeacherProfileUpdateDTOValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(TeacherProfileUpdateDTO teacherProfile, ConstraintValidatorContext context) {
        if (teacherProfile == null)
            return false;

        context.disableDefaultConstraintViolation();

        boolean isValid = true;

        if (!(teacherProfile.getTitle() >= 0 && teacherProfile.getTitle() <= 2)) {
            context.buildConstraintViolationWithTemplate("`Title` must be within [0, 2]").addConstraintViolation();
            return false;
        }

        if (teacherProfile.getRemnant() < 0 || teacherProfile.getRemnant() > teacherProfile.getTotal()) {
            context.buildConstraintViolationWithTemplate("Remnant must be greater than 0 and less than total").addConstraintViolation();
            isValid = false;
        }
        if (teacherProfile.getIdentity() < 0 || teacherProfile.getIdentity() > 5) {
            context.buildConstraintViolationWithTemplate("`Identity` must be within [0, 5]").addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
