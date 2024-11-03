package backend.exception.model.json;

import backend.exception.model.BaseException;

public class JsonConvertionError extends BaseException {
    public JsonConvertionError(Integer code, String msg){
        super(msg, code);
    }
}
