package backend;

import backend.mapper.MajorMapper;
import backend.service.MajorService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("backend.mapper")
public class GradSyncBridgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradSyncBridgeApplication.class, args);
    }

    // @Autowired
    // private MajorService majorService;

    // @Override
    // public void run(String... args) throws Exception {
    // System.out.println(majorService.grabMajors(4));
    // }
}
