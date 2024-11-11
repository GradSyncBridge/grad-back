package backend.enums;

import lombok.Getter;

@Getter
public enum DeadlineEnum {
    INITIAL_SUBMISSION(0, "initial submission"),
    SECOND_SUBMISSION(1, "second submission"),
    FIRST_ENROLL(2, "first enroll"),
    SECOND_ENROLL(3, "second enroll"),
    THIRD_ENROLL(4, "third enroll");

    private final int value;
    private final String description;

    DeadlineEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
