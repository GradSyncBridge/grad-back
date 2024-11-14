package backend.mapper;

import backend.model.VO.subject.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SubjectMapper {

    /**
     * 插入课程
     *
     * @param subject 插入值
     */
    void insertSubject(backend.model.entity.Subject subject);

    /**
     * 查询课程
     *
     * @param subject 查询条件
     * @param scope   查询返回的字段
     * @return 课程列表
     */
    List<backend.model.entity.Subject> selectSubject(backend.model.entity.Subject subject, Map<String, Boolean> scope);

    /**
     * 更新课程
     *
     * @param subjectUpdate 更新值
     * @param subjectQuery  更新的条件
     */
    void updateSubject(backend.model.entity.Subject subjectUpdate, backend.model.entity.Subject subjectQuery);

    List<Subject> selectSubjectForeach(List<Integer> ids);

    Subject selectSubMajorSubject(Integer majorId);
}
