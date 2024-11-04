package backend.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 站内信表
 * @field id 主键 int
 * @field cid 内容ID -> MessageContent.id;
 * @field receiver 收件人 int -> User.id;
 * @field is_read 已读/未读 int (1/0);
 * @field disabled 是否删除 0 -- 删除 1 -- 启用;
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private Integer id;

    // MessageContent.id
    private Integer cid;

    // User.id
    private Integer receiver;

    // 0 -- unread, 1 -- read
    private Integer isRead;

    private Integer disabled;
}
