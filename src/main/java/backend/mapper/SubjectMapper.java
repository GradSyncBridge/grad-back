package backend.mapper;

import backend.model.entity.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SubjectMapper {

    /**
     * 插入课程
     * @param subject 插入值
     */
    void insertSubject(Subject subject);

    /**
     * 查询课程
     * @param subject 查询条件
     * @param scope 查询返回的字段
     * @return 课程列表
     */
    List<Subject> selectSubject(Subject subject, Map<String, Boolean> scope);

    /**
     * 更新课程
     * @param subject 更新值
     * @param scope 更新的条件
     */
    void updateSubject(Subject subject, Map<String, Boolean> scope);
}
