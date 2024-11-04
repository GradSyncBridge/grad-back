package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学科表
 *
 * @field id 主键 int
 * @field name 学科名称 varchar
 * @field mid 学科号码 varchar
 * @field pid 上级学科 int (first class -> null)
 * @field description 概述 / 备注 text default ""
 * @field type 学科类型 varchar default "工学学位"
 * @field total 年度总指标 int
 * @field addition 补充指标 int
 * @field remnant 剩余指标 int default = total + addition - recommend
 * @field year 年度 int
 * @field initial 初试科目 JSON
 * @field interview 复试科目 JSON
 * @field recommend 推免数 int
 * @field disabled 是否废弃 int
 * @field department 所属学院 int
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Major {

    private Integer id;

    private String name;

    private String mid;

    // first class -> null
    private Integer pid;

    private String description;

    private String type;

    private Integer total;

    private Integer addition;

    // default = total + addition - recommend
    private Integer remnant;

    private Integer year;

    private String initial;

    private String interview;

    private Integer recommend;

    private Integer disabled;

    private Integer department;
}
