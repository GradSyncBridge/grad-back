package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审查记录
 *
 * @field id 主键 int
 * @field userId 用户 ID int userId -> User.id
 * @field content 内容 text
 * @field created 创建时间 DateTime
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log {

    private Integer id;

    // userId -> User.id
    private Integer userId;

    private String content;

    private LocalDateTime created;
}
