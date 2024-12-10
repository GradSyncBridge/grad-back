package backend.rabbitMQ;

import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("mq")
public class MessageQueueController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<ResultEntity<Object>> enroll(@RequestBody MessageDTO messageDTO) {
        messageService.send(messageDTO);

        return ResultEntity.success(
                200,
                "Sending message successfully.",
                null
        );
    }
}
