package backend.model.VO.user;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRefreshVO {
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

        public UserRefreshVO build() {
            UserRefreshVO userRefreshVO = new UserRefreshVO();
            userRefreshVO.setAccess(access);
            userRefreshVO.setRefresh(refresh);
            return userRefreshVO;
        }
    }
}
