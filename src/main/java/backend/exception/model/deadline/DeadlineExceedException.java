package backend.exception.model.deadline;

import backend.Enums.DeadlineEnum;
import backend.exception.model.BaseException;

public class DeadlineExceedException extends BaseException {

    public DeadlineExceedException() {
        super();
    }

    public DeadlineExceedException(DeadlineEnum deadlineType, Integer deadlineCode) {
        super(String.format("Deadline of type `%s` is exceeded.", deadlineType.getDescription()), deadlineCode);
    }
}
