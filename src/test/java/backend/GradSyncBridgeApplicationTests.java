package backend;

import backend.util.GlobalLogging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootTest
class GradSyncBridgeApplicationTests {

    @Test
    void contextLoads() {
        GlobalLogging.builder()
                .id(1)
                .userId(1)
                .endpoint("test")
                .operation("test")
                .created(null)
                .build()
                .getThis();
    }

}
