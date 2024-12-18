package backend.exception.model.teacher;

import backend.exception.model.BaseException;

public class TeacherNotFoundException extends BaseException {

    public TeacherNotFoundException() {
        super("Major not found", 404);
    }

    public TeacherNotFoundException(Integer mid) {
        super("Major with id = " + mid + " is not found.", 404);
    }

}
