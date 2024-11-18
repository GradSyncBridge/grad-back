package backend.exception.model.enroll;

import backend.exception.model.BaseException;

public class EnrollDuplicateException extends BaseException {
    public EnrollDuplicateException() {
        super();
    }

    public EnrollDuplicateException(String params, Integer code) {
        super(String.format("Target enroll with params = %s is duplicate", params), code);
    }
}
