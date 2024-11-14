package backend.exception.model.user;

import backend.exception.model.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("user not found", 404);
    }

    public UserNotFoundException(Integer uid, Integer role) {
        super(String.format("%s with uid = %d is not found", role ==1 ? "Student" : "Teacher", uid), 404);
    }
}
