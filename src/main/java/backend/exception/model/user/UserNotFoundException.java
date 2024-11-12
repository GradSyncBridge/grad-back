package backend.exception.model.user;

import backend.exception.model.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("user not found", 404);
    }
}
