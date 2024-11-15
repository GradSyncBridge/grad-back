package backend.exception.model.major;

import backend.exception.model.BaseException;

public class MajorNotFoundException extends BaseException {
    public MajorNotFoundException() {
        super();
    }

    public MajorNotFoundException(Integer mid, Integer code) {
        super(String.format("Major [mid=%d] is not found", mid), code);
    }
}
