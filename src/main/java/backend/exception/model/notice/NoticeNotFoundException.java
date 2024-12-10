package backend.exception.model.notice;

import backend.exception.model.BaseException;

public class NoticeNotFoundException extends BaseException {
    public NoticeNotFoundException() {
        super("Notice not found", 404);
    }
}
