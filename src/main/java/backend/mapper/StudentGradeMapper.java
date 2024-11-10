package backend.mapper;

import backend.model.VO.student.GradeList;
import org.apache.ibatis.annotations.Mapper;
import backend.model.entity.StudentGrade;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentGradeMapper {
    
    /**
     * 插入学生成绩
     * 
     * @param studentGrade 待插入的记录
     */
    void insertStudentGrade(StudentGrade studentGrade);

    /**
     * 查询学生成绩
     * 
     * @param studentGrade 查询条件
     * @param scope 查询返回的字段
     * @return 学生成绩列表
     */
    List<StudentGrade> selectStudentGrade(StudentGrade studentGrade, Map<String, Boolean> scope);

    List<GradeList> selectGradeWithSubject(StudentGrade studentGrade, Integer type);

    /**
     * 更新学生成绩
     * 
     * @param studentGradeUpdate 更新值
     * @param studentGradeQuery 更新的条件
     */
    void updateStudentGrade(StudentGrade studentGradeUpdate, StudentGrade studentGradeQuery);

}
