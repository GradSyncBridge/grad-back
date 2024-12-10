package backend.model.VO.majorToTeacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorToTeacherBase {
    private Integer majorID;
    private String name;
    private String majorNum;
    private Integer total;
}