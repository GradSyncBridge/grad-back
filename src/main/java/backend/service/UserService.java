package backend.service;

import backend.model.DTO.UserLoginDTO;
import backend.model.entity.User;

import java.util.Map;

public interface UserService {
    /**
     * 用户登录
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    Map<String, Object> login(UserLoginDTO userLoginDTO);
}
