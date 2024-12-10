package backend.exception.model.majorToTeacher;

import backend.exception.model.BaseException;

public class MajorToTeacherDuplicateException extends BaseException {

    public MajorToTeacherDuplicateException() {
        super();
    }

    public MajorToTeacherDuplicateException(Integer teacher, Integer major) {
        super(String.format("Teacher [uid=%d] and major [id=%d] already exists", teacher, major), 409);
    }
}
