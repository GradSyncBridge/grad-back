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
 * @field userId userId int
 * @field birth 出生日期 datetime
 * @field address 生源地 varchar
 * @field majorGrad 专业 varchar
 * @field school 毕业院校 varchar
 * @field type 本科学校类型 varchar
 * @field enrollment 考生类别 int
 * @field reassign 是否同意调剂 int
 * @field valid 个人材料是否有效 int (-1 -- 待审查 0 -- 通过, 1 -- 不通过)
 * @field examId 考生号 varchar
 * @field emergency 紧急联系人 varchar
 * @field gradePrimary 初试成绩 float
 * @field gradeSecond 复试成绩 float
 * @field majorApply 复试专业 int
 * @field majorStudy 拟录取方向 varchar
 * @field disabled 是否有效，所有申报的学生信息在当年的录取工作结束后都将视为无效 int
 * @field quality 资格审查文件 varchar
 * @field certifyId 身份证号 varchar
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

    private String majorGrad;

    private String school;

    private String type;

    private Integer enrollment;

    private Integer reassign;

    // -1 -- 待审查 0 -- 通过, 1 -- 不通过
    private Integer valid;

    private String examId;

    private String emergency;

    private Double gradeFirst;

    private Double gradeSecond;

    private Integer majorApply;

    private String majorStudy;

    private Integer disabled;

    private String quality;

    private String certifyId;
}
