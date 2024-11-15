package backend.exception.model.user;

import backend.exception.model.BaseException;

public class UserRoleDeniedException extends BaseException {
    public UserRoleDeniedException() {
        super();
    }

    public UserRoleDeniedException(Integer role, Integer code) {
        super(String.format("User with role = %s is denied", role == 1 ? "student" : "teacher"), code);
    }

    public UserRoleDeniedException(Integer role, Integer code, Integer identity) {
        super(String.format("User with role = %s and identity = %d is denied", role == 1 ? "student" : "teacher", identity), code);
    }
}
