package backend.model.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileVO {

    private String username;

    private String avatar;

    private String name;

    private String email;

    private Integer gender;

    private String phone;
}
