package backend.exception.model.user;

import backend.exception.model.BaseException;

public class LoginFailedException extends BaseException {
    public LoginFailedException() {
        super("Error in username or password", 401);
    }
}
