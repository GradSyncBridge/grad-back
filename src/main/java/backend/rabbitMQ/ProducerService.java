package backend.rabbitMQ;

import backend.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mq")
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }

    public void sendRequest(MessageDTO messageDTO) {
        try {
            String routingKey = "message." + User.getAuth().getId();

            ObjectMapper objectMapper = new ObjectMapper();

            String message = objectMapper.writeValueAsString(messageDTO);

            rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, message);
        }catch (Exception e){
            throw new RuntimeException("Failed to send message");
        }
    }
}
