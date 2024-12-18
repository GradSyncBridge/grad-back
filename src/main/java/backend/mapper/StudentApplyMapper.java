package backend.mapper;


import backend.model.VO.teacher.TeacherVO;
import backend.model.entity.StudentApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentApplyMapper {
    
    /**
     * 插入一条学生志愿申请项
     * 
     * @param studentApply 学生志愿申请项
     */
    void insertStudentApply(StudentApply studentApply);

    /**
     * 查询学生志愿申请项
     * 
     * @param studentApply 查询条件
     * @param scope 查询返回的字段
     * @return 学生志愿申请列表
     */
    List<StudentApply> selectStudentApply(StudentApply studentApply, Map<String, Boolean> scope);

    /**
     * 查询志愿申请项并获取对应的教师字段
     *
     * @param studentApply 查询条件
     * @return 志愿申请项目下的教师字段
     * */
    List<TeacherVO> selectApplicationWithTeacher(StudentApply studentApply);

    /**
     * 更新学生志愿申请项
     * 
     * @param studentApplyUpdate 更新值
     * @param studentApplyQuery 更新的条件
     */
    void updateStudentApply(StudentApply studentApplyUpdate, StudentApply studentApplyQuery);
}
