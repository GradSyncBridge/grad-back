package backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication(scanBasePackages = "backend")
@MapperScan("backend.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class GradSyncBridgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradSyncBridgeApplication.class, args);
    }
}
