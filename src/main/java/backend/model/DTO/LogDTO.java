package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class LogDTO {

    private String endpoint;

    private String operation;

    private String created;

    private String uid;

    private String username;

    private String avatar;

    private String name;

    private String email;

    private String gender;

    private String phone;
}
