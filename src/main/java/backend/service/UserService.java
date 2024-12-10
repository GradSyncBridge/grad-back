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
     * 处理登录请求
     * POST /unauthorized/user/login
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获取用户信息
     * GET /user/profile
     * @return 用户信息
     */
    UserProfileVO getUser();

    /**
     * 刷新token
     * GET /user/refresh
     * @return token
     */
    UserRefreshVO refreshToken();

    /**
     * 处理注册请求
     * POST /unauthorized/user/register
     * @param userRegisterDTO 注册信息
     * @return token
     */
    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

    /**
     * 更新用户信息
     * PUT /user/profile
     * @param userProfileUpdateDTO 用户信息
     * @return 用户信息
     */
    UserProfileUpdateDTO updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO);
}
