package backend.rabbitMQ;

import backend.model.entity.User;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Profile("mq")
public class EnrollService {

    @Autowired
    private EnrollProducer producer;


    public ResponseEntity<ResultEntity<Object>> enroll(Integer studentID) {

        Boolean isEnroll = producer.sendRequest(studentID, User.getAuth().getTeacher());

        if(isEnroll) return ResultEntity.success(200, "Enroll success", null);
        else return ResultEntity.error(400, "Enroll failed", null);
    }
}
