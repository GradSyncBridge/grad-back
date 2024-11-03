package backend.mapper;

import backend.model.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {

    /**
     * 插入消息
     * @param message
     */
    void insertMessage(Message message);

    /**
     * 查询消息
     * @param message 查询条件
     * @param scope 查询返回的字段
     * @return 消息列表
     */
    List<Message> selectMessage(Message message, Map<String, Boolean> scope);

    /**
     * 更新消息
     * @param message 更新值
     * @param scope 更新的条件
     */
    void updateMessage(Message message, Map<String, Boolean> scope);
}
