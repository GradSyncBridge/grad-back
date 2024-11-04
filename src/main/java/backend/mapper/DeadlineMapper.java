package backend.mapper;

import backend.model.entity.Deadline;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeadlineMapper {

    /**
     * 插入截止日期
     * @param deadline 截止日期
     */
    void insertDeadline(Deadline deadline);

    /**
     * 查询截止日期
     * @param deadline 查询条件
     * @param scope 查询返回的字段
     * @return 截止日期列表
     */
    List<Deadline> selectDeadline(Deadline deadline, Map<String, Boolean> scope);

    /**
     * 更新截止日期
     * @param deadlineUpdate 更新值
     * @param deadlineQuery 更新的条件
     */
    void updateDeadline(Deadline deadlineUpdate, Deadline deadlineQuery);
}
