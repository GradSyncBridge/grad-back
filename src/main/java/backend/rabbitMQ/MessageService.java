package backend.rabbitMQ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private ProducerService producerService;

    public void send(MessageDTO messageDTO) {
        producerService.sendRequest(messageDTO);
    }

}
