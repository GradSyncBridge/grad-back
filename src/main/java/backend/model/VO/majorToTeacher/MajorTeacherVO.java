package backend.model.VO.majorToTeacher;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorTeacherVO {
    private Integer majorTeacherID;
    private Integer valid;
    private MajorToTeacherBase parent;
    private MajorToTeacherBase child;
}
