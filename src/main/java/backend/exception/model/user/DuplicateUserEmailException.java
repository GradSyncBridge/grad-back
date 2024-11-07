package backend.exception.model.User;

import backend.exception.model.BaseException;

public class DuplicateUserEmailException extends BaseException {
    public DuplicateUserEmailException() {
        super("Duplicate user's email found.", 4091);
    }
}
