package backend.exception.model.User;

import backend.exception.model.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super("User not found", 404);
    }
}
