package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 科目信息表
 * @field id 主键 int
 * @field sid 学科号码 int
 * @field name 学科名称 varchar
 * @field type 学科类型 int (初试/复试) 0/1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    private Integer id;

    private Integer sid;

    private String name;

    // 0 -> 初试 1 -> 复试
    private Integer type;
}