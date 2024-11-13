package backend.model.VO.student;

import backend.model.VO.teacher.TeacherVO;
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
    private MajorSubject majorApply;
    private List<MajorSubject> majorStudy;
    private String school;
    private String type;
    private List<Quality> quality;
    private Integer enrollment;
    private Integer reassign;
    private Score gradeFirst;
    private Score gradeSecond;
    private List<TeacherVO> application;
}
