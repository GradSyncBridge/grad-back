package backend.model.VO.log;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class LogVO {

    private OperationUser user;

    private String endpoint;

    private Object operation;

    private String created;

    public LogVO() {
        this.user = new OperationUser();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class OperationUser {

        private Integer uid;

        private String username;

        private String avatar;

        private String name;

        private String email;

        private Integer gender;

        private String phone;
    }
}
