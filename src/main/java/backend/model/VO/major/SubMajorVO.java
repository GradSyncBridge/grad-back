package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubMajorVO {

    private Integer id;

    private String name;

    private Integer mid;

    private Integer pid;

    private Integer year;

    private List<Map<String, String>> initials;

    private List<String> interviews;

    private List<TeacherInMajorVO> teacher;

}
