package backend.service;

import backend.model.VO.teacher.TeacherProfileVO;
import backend.model.VO.teacher.TeacherVO;

import java.util.List;

public interface TeacherService {
    /**
     * 获取对应部门教师列表
     * @param department 部门
     * @return 教师列表
     */
    List<TeacherVO> getTeacher(Integer department);

    /**
     * 获取对应二级学科教师列表
     * @param majorID 二级学科
     * @return 教师列表
     */
    List<TeacherVO> getTeachersByCatalogue(Integer majorID);

    /**
     * 获取教师个人信息
     * @param uid 教师uid
     * @return 教师个人信息
     */
    TeacherProfileVO getTeacherProfile(Integer uid);
}
