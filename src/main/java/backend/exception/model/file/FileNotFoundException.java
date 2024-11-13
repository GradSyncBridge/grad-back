package backend.exception.model.file;

import backend.exception.model.BaseException;

public class FileNotFoundException extends BaseException {
    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(Integer id) {
        super(String.format("File with id = %d is not found", id), 404);
    }
}
