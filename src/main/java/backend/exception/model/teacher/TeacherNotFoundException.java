package backend.exception.model.teacher;

import backend.exception.model.BaseException;

public class TeacherNotFoundException extends BaseException {

    public TeacherNotFoundException() {
        super("Teacher not found: ", 401);
    }

}
