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
 * @field userId 用户 ID int userId -> user.id
 * @field endpoint 具体接口 /admin/teacher 等
 * @field operation 具体操作(请求内容)
 * @field created 创建时间 DateTime
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log {

    private Integer id;

    // userId -> user.id
    private Integer userId;

    private  String endpoint;

    private  String operation;

    private LocalDateTime created;
}
