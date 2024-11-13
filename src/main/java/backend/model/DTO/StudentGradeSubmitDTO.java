package backend.model.DTO;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradeSubmitDTO {

    private List<GradeSingleDTO> grades;

    private Integer studentID;

    @Getter
    @Setter
    public static class GradeSingleDTO {
        private Double grade;
        private Integer subjectID;
    }
}
