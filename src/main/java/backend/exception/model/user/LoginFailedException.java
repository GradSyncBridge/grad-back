package backend.exception.model.User;

import backend.exception.model.BaseException;

public class LoginFailedException extends BaseException {
    public LoginFailedException() {
        super("Error in username or password", 401);
    }
}
