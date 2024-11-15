package backend.service;

import backend.model.DTO.StudentApplicationSubmitDTO;
import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.model.VO.student.StudentSubmitTableVO;
import backend.model.VO.user.UserProfileVO;

import java.util.List;

public interface StudentService {

    /**
     * 获取学生提交信息
     * GET /student
     * @param targetUid 目标用户ID
     * @return 学生提交表
     */
    StudentSubmitTableVO getStudentSubmitTable(Integer targetUid);

    /**
     * 搜索学生
     * GET /student/search
     * @param key 关键词
     * @param valid 用户是否有效
     * @return 学生列表
     */
    List<UserProfileVO> searchStudent(String key, Integer valid);

    /**
     * 学生提交/修改报名表单
     * POST /student
     * PUT /student
     * @param submitDTO 提交信息
     */
    void studentSubmit(StudentSubmitDTO submitDTO);

    /**
     * 学生/教师提交成绩信息
     * /student/grade
     * @param submitDTO 学生成绩
     */
    void studentGradeSubmit(StudentGradeSubmitDTO submitDTO);

    /**
     * 学生/教师修改成绩信息
     * PUT /student/grade
     * @param modifyDTO 学生成绩
     */
    void studentGradeModify(StudentGradeModifyDTO modifyDTO);

    /**
     * 学生提交志愿信息
     * POST /student/apply
     * @param submitDTO 学生志愿信息
     */
    void studentApplicationSubmit(StudentApplicationSubmitDTO submitDTO);
}
