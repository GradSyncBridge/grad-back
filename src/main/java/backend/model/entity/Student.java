package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生表
 * @field id 表id int
 * @field userId user_id int
 * @field birth 出生日期 datetime
 * @field grade 成绩 json
 * @field address 生源地 varchar
 * @field major 专业 varchar
 * @field school 毕业院校 varchar
 * @field type 本科学校类型 varchar
 * @field biography 简介路径 varchar
 * @field enrollment 考生类别 int
 * @field application 报考志愿 json
 * @field reassign 是否同意调剂 int
 * @field valid 个人材料是否有效 int (-1 -- 待审查 0 -- 通过, 1 -- 不通过)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private Integer id;

    private Integer userId;

    private LocalDateTime birth;

    private String grade;

    private String address;

    private String major;

    private String school;

    private String type;

    private String biography;

    private Integer enrollment;

    private String application;

    private Integer reassign;

    // -1 -- 待审查 0 -- 通过, 1 -- 不通过
    private Integer valid;

}
