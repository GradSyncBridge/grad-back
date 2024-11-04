package backend.controller;

import backend.annotation.group.UserGroup.EmailGroup;
import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
import backend.model.VO.UserRegisterVO;
import backend.model.entity.User;
import backend.service.UserService;
import backend.util.FileManager;
import backend.util.ResultEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping(value = "/user/email")
    public ResponseEntity<ResultEntity<String>> getEmail(@RequestBody Map<String, String> map) {
        String image = map.get("image");
        FileManager.saveBase64Image(image);
        return ResultEntity.success(HttpStatus.OK.value(), "ok");
    }

    /**
     * 执行用户注册操作
     * @param userRegisterDTO 包含用户注册账号，密码，性别，角色。
     * @return 用户Token
     */
    @PostMapping(value = "/register")
    public ResponseEntity<ResultEntity<Object>> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        UserRegisterVO data = userService.register(userRegisterDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", data);
    }

}
