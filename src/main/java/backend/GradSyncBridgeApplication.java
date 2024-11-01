package backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("backend.mapper")
public class GradSyncBridgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradSyncBridgeApplication.class, args);
    }

}
