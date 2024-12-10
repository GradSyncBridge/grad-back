package backend.annotation.DTO.impl;

import backend.annotation.DTO.AdminTeacherDTOValidation;
import backend.model.DTO.AdminTeacherDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AdminTeacherDTOValidationImpl implements ConstraintValidator<AdminTeacherDTOValidation, AdminTeacherDTO> {
    @Override
    public boolean isValid(AdminTeacherDTO adminTeacherDTO, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (adminTeacherDTO == null) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Null request body")
                    .addConstraintViolation();
            return false;
        }

        if (adminTeacherDTO.getIdentity() < 0 || adminTeacherDTO.getIdentity() > 3) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Identity value must be within 0~3")
                    .addConstraintViolation();
            return false;
        }

        if (adminTeacherDTO.getTitle() < 0 || adminTeacherDTO.getTitle() > 2) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Title must be within 0~2")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
