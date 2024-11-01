package backend.model.entity;

import backend.annotation.UserValidation;
import backend.annotation.group.UserGroup.EmailGroup;
import backend.annotation.group.UserGroup.UsernameGroup;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserValidation(groups = EmailGroup.class)
@UserValidation(groups = UsernameGroup.class)
public class User implements UserDetails{

    private Integer id;

    private String username;

    private String password;

    private String email;

    private String grade;

    // 1 represent student, 2 represents teacher, 3 represents admin
    private Integer role;

    public static User getAuth(){
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
