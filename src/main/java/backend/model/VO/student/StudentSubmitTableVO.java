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
public class StudentSubmitTableVO {
    private String birth;
    private String examID;
    private String certifyID;
    private String emergency;
    private String address;
    private String majorGrad;
    private MajorSubject majorApply; // query the `Major` table
    private List<MajorSubject> majorStudy; // query the `Major` table n times
    private String school;
    private String type;
    private List<Quality> quality; // query the `Quality` table n times
    private Integer enrollment;
    private Integer reassign;
    private Score gradeFirst; // Query the `StudentGrade` table
    private Score gradeSecond; // Query the `StudentGrade` table
    private List<Application> application; // Query both `StudentApply` and `Teacher` table
}
