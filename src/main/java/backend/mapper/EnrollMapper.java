package backend.mapper;

import backend.model.entity.Enroll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EnrollMapper {

    /**
     * 插入一条选课记录
     *
     * @param enroll 选课记录
     */
    void insertEnroll(Enroll enroll);

    /**
     * 查询选课记录
     *
     * @param enroll 查询条件
     * @param scope  查询返回的字段
     * @return 选课记录列表
     */
    List<Enroll> selectEnroll(Enroll enroll, Map<String, Boolean> scope);

    List<Enroll> selectEnrollWithDept(Integer department, Integer year);

    /**
     * 更新选课记录
     *
     * @param enrollUpdate 更新值
     * @param enrollQuery  更新的条件
     */
    void updateEnroll(Enroll enrollUpdate, Enroll enrollQuery);
}
