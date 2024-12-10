package backend.rabbitMQ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mq")
public class MessageService {

    @Autowired
    private ProducerService producerService;

    public void send(MessageDTO messageDTO) {
        producerService.sendRequest(messageDTO);
    }

}
