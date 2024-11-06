package backend.annotation.DTO.impl;

import backend.annotation.DTO.StudentTableDTOValidation;
import backend.model.DTO.StudentTableDTO;
import backend.model.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StudentTableDTOValidationImpl implements ConstraintValidator<StudentTableDTOValidation, StudentTableDTO> {
    @Override
    public void initialize(StudentTableDTOValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(StudentTableDTO studentTableDTO, ConstraintValidatorContext constraintValidatorContext) {
        return User.getAuth().getRole() == 1 && studentTableDTO != null &&
                studentTableDTO.getBirth() != null &&
                studentTableDTO.getExamID() != null && !studentTableDTO.getExamID().isEmpty() &&
                studentTableDTO.getCertifyID() != null && !studentTableDTO.getCertifyID().isEmpty() &&
                studentTableDTO.getEmergency() != null && !studentTableDTO.getEmergency().isEmpty() &&
                studentTableDTO.getAddress() != null && !studentTableDTO.getAddress().isEmpty() &&
                studentTableDTO.getMajorGrad() != null && !studentTableDTO.getMajorGrad().isEmpty() &&
                studentTableDTO.getMajorApply() != null &&
                studentTableDTO.getMajorStudy() != null && !studentTableDTO.getMajorStudy().isEmpty() &&
                studentTableDTO.getSchool() != null && !studentTableDTO.getSchool().isEmpty() &&
                studentTableDTO.getType() != null && !studentTableDTO.getType().isEmpty() &&
                studentTableDTO.getEnrollment() != null &&
                studentTableDTO.getReassign() != null;
    }
}
