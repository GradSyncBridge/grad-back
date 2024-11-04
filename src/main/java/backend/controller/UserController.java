package backend.controller;

import backend.annotation.group.UserGroup.EmailGroup;
import backend.model.DTO.UserLoginDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
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

}
