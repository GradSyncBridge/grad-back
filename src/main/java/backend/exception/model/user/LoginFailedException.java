package backend.exception.model.user;

import backend.exception.model.BaseException;

public class LoginFailedException extends BaseException {
    public LoginFailedException(Integer code, String msg){
        super(msg, code);
    }
}
