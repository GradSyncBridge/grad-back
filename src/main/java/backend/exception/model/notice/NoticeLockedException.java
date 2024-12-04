package backend.exception.model.notice;

import backend.exception.model.BaseException;

public class NoticeLockedException extends BaseException {

    public NoticeLockedException() {
        super("Notice has locked", 409);
    }
}
