package backend.model.VO.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRefUser {

    private Integer uid;

    private String username;

    private String avatar;

    private String name;

    private String email;

    private Integer gender;

    private String phone;
}
