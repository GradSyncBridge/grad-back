package backend.model.VO.major;

import backend.model.VO.subject.SubjectVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorSecondVO {
    private Integer majorID;

    private String name;

    private String majorNum;

    private Integer year;

    private String description;

    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer recommend;

    private Integer department;

    private List<SubjectVO> initials;

    private List<SubjectVO> interviews;
}
