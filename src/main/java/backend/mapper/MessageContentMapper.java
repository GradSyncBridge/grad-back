package backend.mapper;

import backend.model.entity.MessageContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.List;

@Mapper
public interface MessageContentMapper {
    /**
     * 插入新的 message content 记录
     *
     * @param messageContent
     */
    void insertMessageContent(MessageContent messageContent);

    /**
     * 查询消息内容
     *
     * @param message 查询条件
     */
    List<MessageContent> selectMessageContent(MessageContent message, Map<String, Boolean> scope);

    /**
     * 更新消息内容
     *
     * @param messageContentUpdate 更新值
     * @param messageContentQuery  更新条件
     */
    void updateMessageContent(MessageContent messageContentUpdate, MessageContent messageContentQuery);
}
