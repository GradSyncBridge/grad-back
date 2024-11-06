package backend.model.DTO;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeSubmitListDTO {

    private List<GradeSubmitDTO> grades;

    @Getter
    @Setter
    public static class GradeSubmitDTO {
        private Integer grade;
        private Integer subjectID;
    }
}
