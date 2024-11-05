package backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学生表
 *
 * @field id 表id int
 * @field userId user_id int
 * @field birth 出生日期 datetime
 * @field address 生源地 varchar
 * @field major_grad 专业 varchar
 * @field school 毕业院校 varchar
 * @field type 本科学校类型 varchar
 * @field enrollment 考生类别 int
 * @field reassign 是否同意调剂 int
 * @field valid 个人材料是否有效 int (-1 -- 待审查 0 -- 通过, 1 -- 不通过)
 * @field exam_id 考生号 varchar
 * @field emergence 紧急联系人 varchar
 * @field grade_primary 初试成绩 float
 * @field grade_second 复试成绩 float
 * @field major_apply 复试专业 int
 * @field major_study 拟录取方向 varchar
 * @field disabled 是否有效，所有申报的学生信息在当年的录取工作结束后都将视为无效 int
 * @field quality 资格审查文件 varchar
 * @field certify_id 身份证号 varchar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private Integer id;

    private Integer userId;

    private LocalDateTime birth;

    private String address;

    private String major_grad;

    private String school;

    private String type;

    private Integer enrollment;

    private Integer reassign;

    // -1 -- 待审查 0 -- 通过, 1 -- 不通过
    private Integer valid;

    private String exam_id;

    private String emergence;

    private Float grade_primary;

    private Float grade_second;

    private Integer major_apply;

    private String major_study;

    private Integer disabled;

    private String quality;

    private String certify_id;
}
