package backend.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 站内信表
 * @field id 主键 int
 * @field src 发件人 int -> User.id
 * @field dst 收件人 int -> User.id
 * @field created 创建时间 DateTime
 * @field is_read 已读/未读 int (1/0)
 * @field content 站内信内容 text -> MessageContent.content
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private Integer id;

    // User.id
    private Integer src;

    // User.id
    private Integer dst;

    private LocalDateTime created;

    // 0 -- unread, 1 -- read
    private Integer isRead;

    // MessageContent.content
    private String content;
}
