package backend.model.VO;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录VO
 *
 * @field access: access token
 * @field refresh: refresh token
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {

    private String access;

    private String refresh;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String access;

        private String refresh;

        public Builder setToken(Integer id, JwtService jwtService) {
            access = jwtService.generateToken(id, 1);
            refresh = jwtService.generateToken(id, 2);
            return this;
        }

        public UserLoginVO build() {
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setAccess(access);
            userLoginVO.setRefresh(refresh);
            return userLoginVO;
        }
    }
}
