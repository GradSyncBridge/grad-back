package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生志愿申请表
 * @field id 主键 int
 * @user_id 用户 ID int
 * @field tid 教师 ID int
 * @field level 志愿等级(第一/第二/第三志愿) int
 * @field disabled 是否有效 int (0 -> 无效, 1 -> 有效)
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentApply {
    
    private Integer id;

    private Integer user_id;

    private Integer tid;

    private Integer level;

    private Integer disabled;
}
