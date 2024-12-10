package backend.service;

import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.model.VO.teacher.TeacherProfileVO;
import backend.model.VO.teacher.TeacherVO;
import backend.model.VO.user.UserProfileVO;

import java.util.List;

public interface TeacherService {

    /**
     * 获取学院下属所有老师
     * GET /unauthorized/department/teachers
     * @param department 学院
     * @return 老师列表
     */
    List<TeacherVO> getTeacher(Integer department);

    /**
     * 获取对应二级学科教师列表
     * GET /unauthorized/catalogue/teachers
     * @param majorID 二级学科
     * @return 教师列表
     */
    List<TeacherVO> getTeachersByCatalogue(Integer majorID);

    /**
     * 获取教师个人信息
     * GET /teacher/profile
     * @param uid 教师uid
     * @return 教师个人信息
     */
    TeacherProfileVO getTeacherProfile(Integer uid);

    /**
     * 修改教师个人信息
     * PUT /teacher/profile
     * @param teacherProfile 教师个人信息
     */
    TeacherProfileUpdateDTO updateTeacherProfile(TeacherProfileUpdateDTO teacherProfile);

    /**
     * 获取第一/第二/第三志愿选择教师的学生信息
     * GET /teacher/apply
     * @param level 志愿等级
     * @return 学生信息
     */
    List<UserProfileVO> getTeacherApplicationByLevel(Integer level);
}
