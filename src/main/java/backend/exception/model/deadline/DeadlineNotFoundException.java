package backend.exception.model.deadline;

import backend.exception.model.BaseException;

public class DeadlineNotFoundException extends BaseException {
    public DeadlineNotFoundException() {
        super();
    }

    public DeadlineNotFoundException(Integer deadlineType) {
        super(String.format("Target deadline with type = %d is not found.", deadlineType), 404);
    }

    /**
     * Deadline not found exception
     *
     * @param idOrType DeadlineID or DeadlineType
     * @param type 0 -- type, 1 -- deadline id
     * */
    public DeadlineNotFoundException(Integer idOrType, Integer type) {
        super(String.format("Target deadline with %s = %d is not found", type == 0 ? "type" : "id", idOrType), 404);
    }
}
