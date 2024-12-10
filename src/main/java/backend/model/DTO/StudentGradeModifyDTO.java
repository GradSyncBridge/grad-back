package backend.model.DTO;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradeModifyDTO {

    private List<GradeSingleDTO> grades;

    private Integer studentID;

    @Getter
    @Setter
    public static class GradeSingleDTO {
        private Integer gradeID;
        private Double grade;
    }

}
