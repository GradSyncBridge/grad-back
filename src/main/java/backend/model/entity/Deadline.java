package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 截止日期表
 *
 * @field id 主键 int
 * @field time 截止日期 DateTime
 * @field type 截止日期类型 int (0 — 提交个人材料，1 — 第一轮导师选择学生，2 — 第二轮调剂，3 — 录取结束)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deadline {

    private Integer id;

    private LocalDateTime time;

    // 0: 提交初试材料截止；1: 提交复试材料截止；2: 第一轮录取结束；3: 第二轮录取结束；4: 第三轮录取结束
    private Integer type;
}
