package backend.exception.model.enroll;

import backend.exception.model.BaseException;

public class EnrollNotFoundException extends BaseException {

    public EnrollNotFoundException() {
        super();
    }

    public EnrollNotFoundException(Integer id) {
        super(String.format("Enroll [id=%d] is not found", id), 404);
    }
}
