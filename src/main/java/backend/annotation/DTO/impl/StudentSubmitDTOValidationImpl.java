package backend.annotation.DTO.impl;

import backend.annotation.DTO.StudentSubmitDTOValidation;
import backend.model.DTO.StudentSubmitDTO;
import backend.model.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StudentSubmitDTOValidationImpl implements ConstraintValidator<StudentSubmitDTOValidation, StudentSubmitDTO> {
    @Override
    public void initialize(StudentSubmitDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(StudentSubmitDTO submitDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (submitDTO == null)
            return false;

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (User.getAuth().getStudent().getDisabled() == 0) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(String.format("Student with uid = %d is disabled", User.getAuth().getId()))
                    .addConstraintViolation();
            return false;
        }

        if (
            submitDTO.getBirth() != null
            && !submitDTO.getBirth().isEmpty()
            && submitDTO.getExamID() != null
            && !submitDTO.getExamID().isEmpty()
            && submitDTO.getCertifyID() != null
            && !submitDTO.getCertifyID().isEmpty()
            && submitDTO.getEnrollment() >= 0
            && submitDTO.getEnrollment() <= 4
        )
            return true;

        constraintValidatorContext
                .buildConstraintViolationWithTemplate("Student submission table is incomplete, check the required fields")
                .addConstraintViolation();

        return false;
    }
}
