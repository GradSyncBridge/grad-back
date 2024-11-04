package backend.service;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
import backend.model.VO.UserRefreshVO;
import backend.model.VO.UserRegisterVO;

public interface UserService {
    /**
     * 用户登录
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获取用户信息
     * @return 用户信息
     */
    UserProfileVO getUser();

    /**
     * 刷新token
     * @return token
     */
    UserRefreshVO refreshToken();

    UserRegisterVO register(UserRegisterDTO userRegisterDTO);
}
