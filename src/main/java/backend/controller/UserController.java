package backend.controller;

import backend.annotation.group.UserGroup.EmailGroup;
import backend.model.DTO.UserLoginDTO;
import backend.model.entity.User;
import backend.service.UserService;
import backend.util.ResultEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResultEntity<Object>> login(@RequestBody UserLoginDTO userLoginDTO) {
        Map<String, Object> data = userService.login(userLoginDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", data);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ResultEntity<Object>> getUser() {
        return ResultEntity.success(HttpStatus.OK.value(), "ok", null);
    }

    @GetMapping(value = "/email")
    public ResponseEntity<ResultEntity<String>> getEmail(@Validated(EmailGroup.class) @RequestBody User user) {
        return ResultEntity.success(HttpStatus.OK.value(), "ok");
    }

}
