package backend.exception.model.file;

import backend.exception.model.BaseException;

public class FileStorageException extends BaseException {
    public FileStorageException(Integer code, String message) {
        super(message, code);
    }
}
