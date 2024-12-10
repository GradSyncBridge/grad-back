package backend.model.VO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息VO
 *
 * @field username: 用户名
 * @field avatar: 头像
 * @field name: 姓名
 * @field email： 邮箱
 * @field gender 性别
 * @field phone: 电话
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileVO {
    private Integer uid;

    private String username;

    private String avatar;

    private String name;

    private String email;

    private Integer gender;

    private String phone;
}
