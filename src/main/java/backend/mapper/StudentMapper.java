package backend.mapper;

import backend.model.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 插入学生
     *
     * @param student 插入值
     */
    void insertStudent(Student student);

    /**
     * 查询学生
     *
     * @param student 查询条件
     * @param scope   查询返回的字段
     * @return 学生列表
     */
    List<Student> selectStudent(Student student, Map<String, Boolean> scope);

    /**
     * 更新学生
     *
     * @param studentUpdate 更新值
     * @param studentQuery  更新的条件
     */
    void updateStudent(Student studentUpdate, Student studentQuery);

    /**
     * 批量设置学生 valid 字段无效
     *
     * @param students 学生列表
     * */
    void invalidateStudent(List<Student> students);


    /**
     * 根据学院选取不接受调剂 & 未被录取的学生
     *
     * @param department 学院ID
     * */
    List<Student> selectStudentWithoutEnroll(Integer department);
}
