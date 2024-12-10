package backend.rabbitMQ;

import backend.mapper.MessageContentMapper;
import backend.mapper.MessageMapper;
import backend.model.entity.Message;
import backend.model.entity.MessageContent;
import backend.ws.MessageEntity;
import backend.ws.WebSocketServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("mq")
public class ConsumerService {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageContentMapper messageContentMapper;

    @RabbitListener(queues = "message-queue")
    public void process(String message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);

            MessageContent messageContent = MessageContent.builder()
                    .sender(messageDTO.getSender())
                    .disabled(1)
                    .created(LocalDateTime.now())
                    .draft(0)
                    .content(messageDTO.getContent())
                    .build();

            messageContentMapper.insertContent(messageContent);


            Message insertMessage = Message.builder()
                    .receiver(messageDTO.getReceiver())
                    .isRead(0)
                    .cid(messageContent.getId())
                    .disabled(1)
                    .build();

            messageMapper.insertMessage(insertMessage);

            MessageEntity messageEntity = MessageEntity.builder()
                                                .id(insertMessage.getId())
                                                .receiver(insertMessage.getReceiver())
                                                .sender(messageContent.getSender())
                                                .content(messageContent.getContent())
                                                .build();

            String sendMessage = objectMapper.writeValueAsString(messageEntity);

            webSocketServer.sendMessageToAllQualified(sendMessage);
        }catch(Exception e){
            throw new RuntimeException("Failed to process message");
        }
    }

}
