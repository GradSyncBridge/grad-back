package backend.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 站内信内容
 *
 * @field id 主键 int
 * @field content 站内信内容 text
 * @field created 创建时间
 * @field sender 创建人/发件人 user.id
 * @field draft 是否为草稿 是 -- 1 否 -- 0
 * @field disabled 是否删除 删除 -- 0 启用 -- 1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageContent {

    private Integer id;

    private String content;

    private LocalDateTime created;

    // user.id
    private Integer sender;

    private Integer draft;

    private Integer disabled;
}
