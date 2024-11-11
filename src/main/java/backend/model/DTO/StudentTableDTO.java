package backend.model.DTO;

import backend.annotation.DTO.StudentTableDTOValidation;
import backend.model.entity.StudentGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StudentTableDTOValidation
public class StudentTableDTO {
    private Integer targetUid;

    private String birth;

    private LocalDateTime birthday;

    private String examID;

    private String certifyID;

    private String emergency;

    private String address;

    private String majorGrad;

    private Integer majorApply;

    private List<String> majorStudy;

    private String school;

    private String type;

    private List<Integer> quality;

    private Integer enrollment;

    private Integer reassign;

    private List<StudentGrade> grades;
}
