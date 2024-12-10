package backend.model.VO.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeList {

    private Integer gradeID;

    private Integer grade;

    private Integer subjectID;

    private String subjectNum;

    private String name;
}
