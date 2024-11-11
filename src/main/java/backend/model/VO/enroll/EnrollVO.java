package backend.model.VO.enroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EnrollVO {
    private Integer enrollmentID;
    private String student;
    private String teacher;
    private Integer gender;
    private String departmentNum;
    private String departmentName;
    private String majorNum;
    private String majorName;
}
