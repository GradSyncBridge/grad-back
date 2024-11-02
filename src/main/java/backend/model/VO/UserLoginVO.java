package backend.model.VO;

import backend.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {
    private Map<String, Object> token;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String accessToken;

        private String refreshToken;

        public Builder setToken(String username, JwtService jwtService) {
            accessToken = jwtService.generateToken(username, 1);
            refreshToken = jwtService.generateToken(username, 2);
            return this;
        }

        public UserLoginVO build() {
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setToken(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
            return userLoginVO;
        }
    }
}
