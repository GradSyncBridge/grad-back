package backend.model.json;

import backend.exception.model.json.JsonConvertionError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MajorInitialJson {

    private ArrayList<String> initial;

    public static MajorInitialJson fromJsonString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, MajorInitialJson.class);
        } catch (Exception e) {
            throw new JsonConvertionError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new JsonConvertionError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

}
