package backend.rabbitMQ;

import backend.rabbitMQ.EnrollDTO;
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
    private EnrollService enrollService;

    @PostMapping("/enroll")
    public ResponseEntity<ResultEntity<Object>> enroll(@RequestBody EnrollDTO enrollDTO) {
        return enrollService.enroll(enrollDTO.getStudentID());
    }
}
