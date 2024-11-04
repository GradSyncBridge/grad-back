package backend.model.VO;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

        public Builder setToken(String username, JwtService jwtService) {
            access = jwtService.generateToken(username, 1);
            refresh = jwtService.generateToken(username, 0);
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
