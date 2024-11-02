package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审查记录
 * @field id 主键 int
 * @field user_id 用户 ID int user_id -> User.id
 * @field content 内容 text
 * @field created 创建时间 DateTime
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log {

    private Integer id;

    // user_id -> User.id
    private Integer userId;

    private String content;

    private String created;
}
