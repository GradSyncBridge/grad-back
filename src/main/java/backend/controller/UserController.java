package backend.controller;

import backend.annotation.group.UserGroup.EmailGroup;
import backend.config.JwtService;
import backend.exception.model.LoginFailedException;
import backend.model.DTO.UserDTO;
import backend.model.converter.UserConverter;
import backend.model.entity.User;
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
    private UserConverter userConverter;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResultEntity<Object>> login() {
        User user = User.builder().username("Bush").build();
        if (user == null)
            throw new LoginFailedException(HttpStatus.BAD_REQUEST.value(), "Login failed");
         String token = jwtService.generateToken(user.getUsername());
         return ResultEntity.success(HttpStatus.OK.value(), "ok", Map.of("token", token));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ResultEntity<Object>> getUser() {
        User user = User.getAuth();
        UserDTO userDTO = userConverter.INSTANCE.UserToUserDTO(user);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", userDTO);
    }

    @GetMapping(value = "/email")
    public ResponseEntity<ResultEntity<String>> getEmail(@Validated(EmailGroup.class) @RequestBody User user) {
        return ResultEntity.success(HttpStatus.OK.value(), "ok");
    }

}
