package backend.exception.model.file;

import backend.exception.model.BaseException;

public class UnsupportedFileFormatException extends BaseException {
    public UnsupportedFileFormatException(Integer code, String message) {
        super(message, code);
    }
}
