package backend.exception.model.teacher;

import backend.exception.model.BaseException;

public class TeacherProfileNoRightException extends BaseException {
    public TeacherProfileNoRightException(){
        super("No right to get teacher's profile.", 401);
    }
}
