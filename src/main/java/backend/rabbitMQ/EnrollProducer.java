package backend.rabbitMQ;

import backend.model.entity.Teacher;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mq")
public class EnrollProducer {
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Autowired
    public EnrollProducer(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }


    public Boolean sendRequest(Integer studentID, Teacher teacher) {
        String routingKey = "enroll." + studentID;
        String result = (String) rabbitTemplate.convertSendAndReceive(topicExchange.getName(), routingKey, studentID + " " + teacher.getUserId());

        return result != null;
    }
}
