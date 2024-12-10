package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorVO {
    private Integer majorID;

    private String name;

    private String majorNum;

    private String description;

    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer recommend;

    private Integer year;

    private Integer department;

    private List<Integer> initials;

    private List<Integer> interviews;

    private List<SubMajorVO> subMajors;
}
