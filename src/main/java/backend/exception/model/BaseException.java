package backend.exception.model;

public class BaseException extends RuntimeException {

    private Integer code;
    public BaseException() {
    }

    public Integer getCode() {
        return code;
    }

    public BaseException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

}
