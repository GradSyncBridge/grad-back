package backend.enums;

import lombok.Getter;

@Getter
public enum DeadlineEnum {
    INITIAL_SUBMISSION(0, "initial submission"),
    SECOND_SUBMISSION(1, "second submission"),
    ENROLL(2, "enroll");

    private final int value;
    private final String description;

    DeadlineEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
