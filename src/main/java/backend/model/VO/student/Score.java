package backend.model.VO.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score {
    private Integer gradeTotal;

    private List<GradeList> gradeList;
}
