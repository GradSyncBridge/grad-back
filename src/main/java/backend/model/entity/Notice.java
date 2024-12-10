package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公告表
 *
 * @field id 主键 int
 * @field content 内容 text
 * @field created 创建时间 DateTime
 * @field disabled 是否有效 int (0 -> 有效, 1 -> 无效)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    private Integer id;

    private Integer uid;

    private String title;

    private String content;

    private String files;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer draft;

    private Integer publish;

    private Integer disabled;
}
