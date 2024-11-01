package backend.exception.model;

public class LoginFailedException extends BaseException{
    public LoginFailedException(String msg){
        super(msg);
    }
}
