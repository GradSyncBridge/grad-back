package backend.exception.model.deadline;

import backend.enums.DeadlineEnum;
import backend.exception.model.BaseException;

public class DeadlineUnreachedException extends BaseException {

    public DeadlineUnreachedException() {
        super();
    }

    public DeadlineUnreachedException(DeadlineEnum type, Integer code) {
        super(String.format("Deadline with [type=%s] is not reached", type.getDescription()), code);
    }

}
