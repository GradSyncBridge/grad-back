package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生成绩表
 * 
 * @field id 主键 int
 * @field user_id 用户 ID int
 * @field sid 学科 ID int
 * @field grade 成绩 float
 * @field disabled 是否有效 int (0 -> 无效, 1 -> 有效)
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGrade {
    
    private Integer id;

    private Integer user_id;

    private Integer sid;

    private float grade;

    private Integer disabled;
}
