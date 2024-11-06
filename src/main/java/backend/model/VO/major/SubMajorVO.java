package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubMajorVO {

    private Integer majorID;

    private String name;

    private String majorNum;

    private Integer pid;

    private Integer year;

    private String description;

    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer recommend;

    private Integer department;

    private List<SubMajorSubject> initials;

    private List<SubMajorSubject> interviews;
}
