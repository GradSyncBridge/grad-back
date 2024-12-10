package backend.mapper;

import backend.model.entity.Major;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MajorMapper {

    /**
     * 插入一个专业
     *
     * @param major 专业
     */
    void insertMajor(Major major);

    /**
     * 查询专业
     *
     * @param major 查询条件
     * @param scope 查询返回的字段
     * @return 专业列表
     */
    List<Major> selectMajor(Major major, Map<String, Boolean> scope);

    /**
     * 更新专业
     *
     * @param majorUpdate 更新值
     * @param majorQuery  更新的条件
     */
    void updateMajor(Major majorUpdate, Major majorQuery);
}
