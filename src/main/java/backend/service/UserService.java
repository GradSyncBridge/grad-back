package backend.service;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
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
     * 注册用户信息
     * @param userRegisterDTO
     * @return 正常注册返回200 和 用户Token; 用户名重复返回 409
     */
    UserRegisterVO register(UserRegisterDTO userRegisterDTO);
}
