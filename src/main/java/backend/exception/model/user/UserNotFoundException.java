package backend.exception.model.user;

import backend.exception.model.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("User not found", 404);
    }
}
