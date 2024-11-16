package backend;

import backend.mapper.LogMapper;
import backend.model.entity.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class GradSyncBridgeApplicationTests {

    @Autowired
    LogMapper logMapper;

    @Test
    void contextLoads() {
//        2024-11-16 13:01:22
        Log log = Log.builder().userId(59).created(LocalDateTime.now()).operation("2").endpoint("2").build();
        logMapper.updateLog(log, Log.builder().id(2)
                .operation("12345678").endpoint("0987654321")
                .userId(4).build());
    }

}
