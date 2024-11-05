package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学科-老师表
 *
 * @field id 主键 int
 * @field tid 教师 ID int
 * @field mid 学科 ID int
 * @field valid 是否具有招生资格 int (0 -> false, 1 -> true)
 * @field metric 招生指标,教师在该学科的指标 int 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorToTeacher {

    private Integer id;

    private Integer tid;

    private Integer mid;

    // 0 -> false, 1 -> true
    private Integer valid;

    private Integer metric;

}
