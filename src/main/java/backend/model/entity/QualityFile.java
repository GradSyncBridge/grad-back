package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 资格审查文件
 *
 * @field id 主键 int
 * @field user_id 用户 ID int
 * @field file 文件的相对路径 String
 * @field created 创建时间 DateTime
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QualityFile {

    private Integer id;

    private Integer user_id;

    private String file;

    // 0 -> 有效, 1 -> 无效
    private LocalDateTime created;
}
