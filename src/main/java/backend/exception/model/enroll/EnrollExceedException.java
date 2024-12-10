package backend.exception.model.enroll;

import backend.exception.model.BaseException;

public class EnrollExceedException extends BaseException {

    public EnrollExceedException() {
        super();
    }

    public EnrollExceedException(Integer uid, Integer mid, Integer code) {
        super(String.format("Teacher [uid=%d] at major [mid=%d] has no remaining metric", uid, mid), code);
    }
}
