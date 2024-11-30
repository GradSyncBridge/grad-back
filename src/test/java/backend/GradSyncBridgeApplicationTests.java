package backend;

import backend.model.DTO.EnrollConfirmDTO;
import backend.service.impl.EnrollServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.CompletableFuture;

@EnableAspectJAutoProxy
@SpringBootTest
class GradSyncBridgeApplicationTests {

    @Autowired
    private EnrollServiceImpl enrollService;
    @Test
    void contextLoads() {
        EnrollConfirmDTO.builder().studentID(47).majorID(2).build();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                enrollService.enrollConfirmById(EnrollConfirmDTO.builder().studentID(59).majorID(2).build(), 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> future2 =CompletableFuture.runAsync(() -> {
            try {
                enrollService.enrollConfirmById(EnrollConfirmDTO.builder().studentID(59).majorID(2).build(), 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> future3 =CompletableFuture.runAsync(() -> {
            try {
                enrollService.enrollConfirmById(EnrollConfirmDTO.builder().studentID(59).majorID(2).build(), 5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.allOf(future1, future2, future3).join();
    }

}
