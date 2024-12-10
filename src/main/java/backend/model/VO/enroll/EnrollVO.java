package backend.model.VO.enroll;

import backend.model.VO.department.DepartmentVO;
import backend.model.VO.major.MajorFirstVO;
import backend.model.VO.user.UserProfileVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollVO {
    private Integer enrollmentID;

    private UserProfileVO student;

    private UserProfileVO teacher;

    private DepartmentVO department;

    private MajorFirstVO major;
}
