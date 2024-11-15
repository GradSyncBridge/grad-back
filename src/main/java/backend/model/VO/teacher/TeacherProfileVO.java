package backend.model.VO.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherProfileVO {

    private Integer uid;

    private String username;

    private String avatar;

    private String name;

    private String email;

    private Integer gender;

    private String phone;

    private Integer department;

    private Integer title;

    private String description;

    private Integer total;

    private Integer remnant;

    private Integer identity;
}
