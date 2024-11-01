package backend.exception.model;

public class LoginFailedException extends BaseException{
    public LoginFailedException(Integer code, String msg){
        super(msg, code);
    }
}
