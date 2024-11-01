package backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentGrades {

    private First first;

    private Second second;

    @Getter
    @Setter
    @ToString
    public class First {
        private Integer major;
        private Integer score;

        public First() {}

        public void setFirst(Integer major, Integer score) {
            this.major = major;
            this.score = score;
        }
    }

    @Getter
    @Setter
    @ToString
    public class Second {
        private Integer major;
        private Integer score;

        public Second() {}

        public void setSecond(Integer major, Integer score) {
            this.major = major;
            this.score = score;
        }
    }

    public First getFirst() {
        if (first == null) {
            first = new First();
        }
        return first;
    }

    public Second getSecond() {
        if (second == null) {
            second = new Second();
        }
        return second;
    }
}
