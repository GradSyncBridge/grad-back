package backend.model.VO.user;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册VO
 *
 * @field access: access token
 * @field refresh: fresh token
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterVO {
    private String access;

    private String refresh;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String access;

        private String refresh;

        public Builder setToken(Integer userId, Integer userRole,  JwtService jwtService) {
            access = jwtService.generateToken(userId, userRole, 1);
            refresh = jwtService.generateToken(userId, userRole, 2);
            return this;
        }

        public UserRegisterVO build() {
            UserRegisterVO userRegisterVO = new UserRegisterVO();
            userRegisterVO.setAccess(access);
            userRegisterVO.setRefresh(refresh);
            return userRegisterVO;
        }
    }
}
