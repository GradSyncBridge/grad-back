package backend.mapper;

import backend.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogMapper {

    /**
     * 插入日志
     *
     * @param log 日志
     */
    void insertLog(Log log);

    /**
     * 查询日志
     *
     * @param log   查询条件
     * @param scope 查询返回的字段
     * @return 日志列表
     */
    List<Log> selectLog(Log log, Map<String, Boolean> scope);

    /**
     * 更新日志
     *
     * @param logUpdate 更新值
     * @param logQuery  更新条件字段
     */
    void updateLog(Log logUpdate, Log logQuery);
}
