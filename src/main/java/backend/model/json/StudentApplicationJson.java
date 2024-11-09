package backend.model.json;

import backend.exception.model.json.JsonConvertionError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentApplicationJson {

    private Integer first;

    private Integer second;

    private Integer third;

    public StudentGradesJson getEntity(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (json != null && !json.isEmpty()) {
            try {
                return objectMapper.readValue(json, StudentGradesJson.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new JsonConvertionError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
