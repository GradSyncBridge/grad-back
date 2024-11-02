package backend.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信内容
 * @field id 主键 int
 * @field content 站内信内容 text
 * @field mid 站内信 ID int mid -> Message.id
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageContent {

    private Integer id;

    private String content;

    // mid -> Message.id
    private Integer mid;
}
