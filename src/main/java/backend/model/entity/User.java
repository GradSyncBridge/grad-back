package backend.model.entity;

import backend.annotation.entity.UserValidation;
import backend.annotation.entity.group.UserGroup.EmailGroup;
import backend.annotation.entity.group.UserGroup.UsernameGroup;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户表
 *
 * @field id user id int
 * @field username 用户昵称 unique int
 * @field name 用户名 string
 * @field password 用户密码 string
 * @field email 用户邮箱 string
 * @field avatar 用户头像 string
 * @field role 用户角色 int (1 -- student, 2-- teacher)
 * @field phone 用户电话 string
 * @field gender 用户性别 string
 * @field disabled 用户是否被禁用 int (1 -- active, 0 -- disabled)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserValidation(groups = EmailGroup.class)
@UserValidation(groups = UsernameGroup.class)
public class User implements UserDetails {

    private Integer id;

    private String username;

    private String name;

    private String password;

    private String email;

    private String avatar;

    // 1 -- student, 2-- teacher
    private Integer role;

    private String phone;

    private Integer gender;

    // 1 -- active, 0 -- disabled
    private Integer disabled;

    private Student student;

    private Teacher teacher;

    public static User getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
