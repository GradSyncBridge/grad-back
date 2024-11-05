package backend.service;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.user.UserLoginVO;
import backend.model.VO.user.UserProfileVO;
import backend.model.VO.user.UserRefreshVO;
import backend.model.VO.user.UserRegisterVO;

public interface UserService {
    /**
     * 用户登录
     *
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserProfileVO getUser();

    /**
     * 刷新token
     *
     * @return token
     */
    UserRefreshVO refreshToken();

    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

    UserProfileUpdateDTO updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO);
}
