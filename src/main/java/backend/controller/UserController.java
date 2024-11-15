package backend.controller;

import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.VO.user.UserProfileVO;
import backend.model.VO.user.UserRefreshVO;
import backend.service.UserService;
import backend.util.ResultEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * GET /user/profile
     * @return 用户信息
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> getUser() {
        UserProfileVO user = userService.getUser();
        return ResultEntity.success(HttpStatus.OK.value(), "Get user's profile successfully.", user);
    }

    /**
     * 刷新token
     * GET /user/refresh
     * @return token
     */
    @GetMapping(value = "/refresh")
    public ResponseEntity<ResultEntity<Object>> refreshToken() {
        UserRefreshVO userRefreshVO = userService.refreshToken();
        return ResultEntity.success(HttpStatus.OK.value(), "Refresh user's token successfully.", userRefreshVO);
    }


    /**
     * 更新用户信息
     * PUT /user/profile
     * @param userProfileUpdateDTO 用户信息
     * @return 用户信息
     */
    @PutMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> updateUserProfile(@RequestBody @Validated UserProfileUpdateDTO userProfileUpdateDTO) {
        UserProfileUpdateDTO data = userService.updateUserProfile(userProfileUpdateDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Update user's profile successfully.", data);
    }

}
