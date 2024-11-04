package backend.service;

import backend.model.DTO.UserLoginDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;

public interface UserService {
    /**
     * 用户登录
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    UserProfileVO getUser();
}
