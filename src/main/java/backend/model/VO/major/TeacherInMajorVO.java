package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInMajorVO {

    private Integer userId;

    private String title;

    private String name;

    private String remnant;

    private String description;

}
