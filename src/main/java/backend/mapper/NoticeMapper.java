package backend.mapper;

import backend.model.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    /**
     * 插入一条公告
     * @param notice 公告
     */
    void insertNotice(Notice notice);

    /**
     * 查询公告
     * @param notice 查询条件
     * @param scope 查询返回的字段
     * @return 公告列表
     */
    List<Notice> selectNotice(Notice notice, Map<String, Boolean> scope);

    /**
     * 更新公告
     * @param scope 更新的条件
     */
    void updateNotice(Notice notice, Map<String, Boolean> scope);
}
