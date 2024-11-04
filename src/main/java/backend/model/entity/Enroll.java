package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 录取关系表
 *
 * @field id 主键 int
 * @field sid 学生 ID int
 * @field tid 教师 ID int
 * @field year 年份 int
 * @field disabled 是否有效 管理员 int (0 -> false, 1 -> true)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enroll {

    private Integer id;

    private Integer sid;

    private Integer tid;

    private Integer year;

    // 0 -> false, 1 -> true
    private Integer disabled;
}
