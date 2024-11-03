package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师表
 * @field id 表id int
 * @field title 职称 int
 * @field description 描述 text
 * @field identity 权限 int
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {

    private Integer id;

    private Integer title;

    private String description;

    private Integer identity;
}
