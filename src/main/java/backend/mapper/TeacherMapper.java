package backend.mapper;

import backend.model.VO.teacher.TeacherProfileVO;
import backend.model.entity.MajorToTeacher;
import backend.model.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeacherMapper {

    /**
     * 插入教师
     *
     * @param teacher 插入值
     */
    void insertTeacher(Teacher teacher);

    /**
     * 查询教师
     *
     * @param teacher 查询条件
     * @param scope   查询返回的字段
     * @return
     */
    List<Teacher> selectTeacher(Teacher teacher, Map<String, Boolean> scope);


    /**
     * 更新教师
     *
     * @param teacherUpdate 更新条件
     * @param teacherQuery  查询条件
     */
    void updateTeacher(Teacher teacherUpdate, Teacher teacherQuery);

    List<Teacher> selectTeacherForeach(List<MajorToTeacher> majorToTeachers);

    /**
     * 查询所有指定学院的教师
     *
     * @param department 学院ID
     * @param remnant 1 -- 筛选 remnant 大于 0 的教师，0 -- 不对 remnant 筛选
     * */
    List<TeacherProfileVO> selectTeacherWithMetric(Integer department, Integer remnant);
}
