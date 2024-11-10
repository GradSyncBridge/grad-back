package backend.exception.model.user;

import backend.exception.model.BaseException;

public class DuplicateUserException extends BaseException {
    public DuplicateUserException() {
        super("Duplicate user found.", 409);
    }
}
