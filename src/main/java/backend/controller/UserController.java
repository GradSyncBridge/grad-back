package backend.controller;

import backend.model.DTO.UserLoginDTO;
import backend.model.VO.UserProfileVO;
import backend.model.VO.UserRefreshVO;
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
     * @return 用户信息
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> getUser() {
        UserProfileVO user = userService.getUser();
        return ResultEntity.success(HttpStatus.OK.value(), "ok", user);
    }

    /**
     * 刷新token
     * @return token
     */
    @GetMapping(value = "/refresh")
    public ResponseEntity<ResultEntity<Object>> refreshToken() {
        UserRefreshVO userRefreshVO = userService.refreshToken();
        return ResultEntity.success(HttpStatus.OK.value(), "ok", userRefreshVO);
    }

    @GetMapping(value = "/user/email")
    public ResponseEntity<ResultEntity<String>> getEmail(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        return ResultEntity.success(HttpStatus.OK.value(), "ok");
    }

}
