package backend.exception.model.enroll;

import backend.exception.model.BaseException;

public class EnrollInvalidException extends BaseException {

    public EnrollInvalidException() {
        super();
    }

    public EnrollInvalidException(Integer uid, Integer mid, Integer code) {
        super(String.format("Teacher [uid=%d] is invalid at major [mid=%d]", uid, mid), code);
    }
}
