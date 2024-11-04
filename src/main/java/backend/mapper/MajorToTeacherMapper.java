package backend.mapper;

import backend.model.entity.MajorToTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MajorToTeacherMapper {

    /**
     * 插入老师与专业的关系
     *
     * @param majorToTeacher 老师与专业的关系
     */
    void insertMajorToTeacher(MajorToTeacher majorToTeacher);

    /**
     * 查询老师与专业的关系
     *
     * @param majorToTeacher 查询条件
     * @param scope          查询返回的字段
     * @return MajorToTeacher 列表
     */
    List<MajorToTeacher> selectMajorToTeacher(MajorToTeacher majorToTeacher, Map<String, Boolean> scope);

    /**
     * 更新老师与专业的关系
     *
     * @param majorToTeacherUpdate 更新值
     * @param majorToTeacherQuery  更新的条件
     */
    void updateMajorToTeacher(MajorToTeacher majorToTeacherUpdate, MajorToTeacher majorToTeacherQuery);


}
