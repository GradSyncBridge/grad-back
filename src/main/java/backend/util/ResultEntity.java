package backend.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultEntity<T>{

    private int code;
    private String message;
    private T data;

    public static <T> ResponseEntity<ResultEntity<T>> success(Integer code, String message, T data) {
        ResultEntity<T> resultEntity = new ResultEntity<>(code, message, data);
        return ResponseEntity.status(HttpStatus.OK).body(resultEntity);
    }

    public static <T> ResponseEntity<ResultEntity<T>> success(Integer code, String message) {
        ResultEntity<T> resultEntity = new ResultEntity<>(code, message, null);
        return ResponseEntity.status(HttpStatus.OK).body(resultEntity);
    }

    public static <T> ResponseEntity<ResultEntity<T>> error(Integer code, String message, T data) {
        ResultEntity<T> resultEntity = new ResultEntity<>(code, message, data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultEntity);
    }

    public static <T> ResponseEntity<ResultEntity<T>> error(Integer code, String message) {
        ResultEntity<T> resultEntity = new ResultEntity<>(code, message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultEntity);
    }

    public static <T> ResponseEntity<ResultEntity<T>> error(String message) {
        ResultEntity<T> resultEntity = new ResultEntity<>(HttpStatus.BAD_REQUEST.value(), message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultEntity);
    }
}
