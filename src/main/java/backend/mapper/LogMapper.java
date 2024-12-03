package backend.mapper;

import backend.model.DTO.LogDTO;
import backend.model.entity.Log;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

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
     * @return 日志列表
     */
    Page<LogDTO> selectLog();

    /**
     * 更新日志
     *
     * @param logUpdate 更新值
     * @param logQuery  更新条件字段
     */
    void updateLog(Log logUpdate, Log logQuery);
}
