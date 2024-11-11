package backend.exception.model.deadline;

import backend.exception.model.BaseException;

public class DeadlineNotFoundException extends BaseException {
    public DeadlineNotFoundException() {
        super();
    }

    public DeadlineNotFoundException(Integer deadlineType) {
        super(String.format("Target deadline with type = %d is not found.", deadlineType), 404);
    }
}
