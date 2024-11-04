package backend.controller;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserRegisterVO;
import backend.service.UserService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * UnAuthController
 * @function 处理与身份验证无关的请求
 * @function login 处理登录请求
 */
@RestController
@RequestMapping(value = "/unauthorized")
public class UnAuthController {

    @Autowired
    private UserService userService;

    /**
     * 处理登录请求
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping(value = "/user/login")
    public ResponseEntity<ResultEntity<Object>> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        UserLoginVO data = userService.login(userLoginDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", data);
    }

    /**
     * 处理注册请求
     * @param userRegisterDTO 注册信息
     * @return token
     */
    @PostMapping(value = "/user/register")
    public ResponseEntity<ResultEntity<Object>> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        UserRegisterVO data = userService.register(userRegisterDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", data);
    }

}
