package backend.controller;

import backend.model.DTO.UserLoginDTO;
import backend.model.VO.UserLoginVO;
import backend.service.UserService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResultEntity<Object>> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO data = userService.login(userLoginDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", data);
    }
}
