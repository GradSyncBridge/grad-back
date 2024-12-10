package backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;


@SpringBootApplication(scanBasePackages = "backend")
@MapperScan("backend.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableWebSocket
public class GradSyncBridgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradSyncBridgeApplication.class, args);
    }
}
