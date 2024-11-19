package backend.service;

import backend.model.DTO.SecretaryExamineDTO;
import backend.model.DTO.SecretaryGradeDTO;

public interface SecretaryService {
    /**
     * 研究生管理秘书审核学生报名表单
     * POST /secretary/examine
     * @param examineDTO 学生报名表单审核信息
     */
    void examineStudentSubmission(SecretaryExamineDTO examineDTO);

    /**
     * 研究生管理秘书修改初试成绩
     * PUT /secretary/grade
     * @param gradeDTO 学生成绩信息
     */
    void modifyStudentGrade(SecretaryGradeDTO gradeDTO);
}
