package backend.model.VO;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        public Builder setToken(String username, JwtService jwtService) {
            access = jwtService.generateToken(username, 1);
            refresh = jwtService.generateToken(username, 0);
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
