package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学院信息
 *
 * @field id 主键 int
 * @field did 学院号码 varchar
 * @field name 学院名称 varchar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {
    private Integer id;

    private Integer did;

    private String name;

    private String description;

    private String logo;
}
