package backend.exception.model.user;

import backend.exception.model.BaseException;

public class RegisterFailedException extends BaseException {
    public RegisterFailedException(Integer code, String msg){
        super(msg, code);
    }
}
