package backend.annotation.DTO.impl;

import backend.annotation.DTO.NoticeCreateValidation;
import backend.exception.model.user.UserRoleDeniedException;
import backend.model.DTO.NoticeCreateDTO;
import backend.model.DTO.UserLoginDTO;
import backend.model.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoticeCreateValidationImpl implements ConstraintValidator<NoticeCreateValidation, NoticeCreateDTO> {
    @Override
    public void initialize(NoticeCreateValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(NoticeCreateDTO noticeCreateDTO, ConstraintValidatorContext context) {
        if (noticeCreateDTO == null)
            return false;

        context.disableDefaultConstraintViolation();

        if(User.getAuth().getTeacher() == null || User.getAuth().getTeacher().getIdentity() != 3){
            context.buildConstraintViolationWithTemplate("User is not authorized to create notice")
                    .addConstraintViolation();

            throw new UserRoleDeniedException();
        }

        if( noticeCreateDTO.getNoticeContent() == null || noticeCreateDTO.getNoticeContent().isEmpty() ||
            noticeCreateDTO.getNoticeTitle() == null || noticeCreateDTO.getNoticeTitle().isEmpty() ||
            noticeCreateDTO.getPublish() == null || noticeCreateDTO.getPublish() > 1 || noticeCreateDTO.getPublish() < 0 ||
            noticeCreateDTO.getDraft() == null || noticeCreateDTO.getDraft() > 1 || noticeCreateDTO.getDraft() < 0
        ){
            context.buildConstraintViolationWithTemplate("Invalid notice")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
