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
public class StudentGradesJson {

    private Primary primary;

    private Secondary secondary;

    public StudentGradesJson getEntity(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (json != null && !json.equals("")) {
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

    public class Primary {
        private Integer sid;
        private Integer grade;

        public Primary() {
        }

        public Primary(Integer sid, Integer grade) {
            this.sid = sid;
            this.grade = grade;
        }

        public Integer getSid() {
            return sid;
        }

        public void setSid(Integer sid) {
            this.sid = sid;
        }

        public Integer getGrade() {
            return grade;
        }

        public void setGrade(Integer grade) {
            this.grade = grade;
        }

        public String toString() {
            return "sid=" + this.getSid() + ", grade=" + this.getGrade();
        }
    }

    public class Secondary {
        private Integer sid;
        private Integer grade;

        public Secondary() {
        }

        public Secondary(Integer sid, Integer cid) {
            this.sid = sid;
            this.grade = cid;
        }

        public Integer getSid() {
            return sid;
        }

        public void setSid(Integer sid) {
            this.sid = sid;
        }

        public Integer getGrade() {
            return grade;
        }

        public void setGrade(Integer grade) {
            this.grade = grade;
        }

        public String toString() {
            return "sid=" + this.getSid() + ", grade=" + this.getGrade();
        }
    }

    public Primary getPrimary() {
        if (primary == null)
            return new Primary();
        return primary;
    }

    public void setPrimary(Primary primary) {
        this.primary = primary;
    }

    public Secondary getSecondary() {
        if (secondary == null)
            return new Secondary();
        return secondary;
    }

    public void setSecondary(Secondary secondary) {
        this.secondary = secondary;
    }

    public String toString() {
        return "StudentGradesJson(primary=(" + this.getPrimary() + "), secondary=(" + this.getSecondary() + "))";
    }
}
