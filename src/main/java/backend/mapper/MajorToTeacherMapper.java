package backend.mapper;

import backend.model.entity.MajorToTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MajorToTeacherMapper {

    /**
     * 插入老师与专业的关系
     * @param majorToTeacher 老师与专业的关系
     */
    void insertMajorToTeacher(MajorToTeacher majorToTeacher);

    /**
     * 查询老师与专业的关系
     * @param majorToTeacherMapper 查询条件
     * @param scope 查询返回的字段
     * @return MajorToTeacher列表
     */
    List<MajorToTeacher> selectMajorToTeacher(MajorToTeacherMapper majorToTeacherMapper, Map<String, Boolean> selectScope);

    /**
     * 更新老师与专业的关系
     * @param majorToTeacher 更新值
     * @param scope 更新的条件
     */
    void updateMajorToTeacher(MajorToTeacher majorToTeacher, Map<String, Boolean> scope);


}
