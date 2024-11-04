package backend.service.impl;

import backend.config.JwtService;
import backend.exception.model.user.LoginFailedException;
import backend.mapper.UserMapper;
import backend.model.DTO.UserLoginDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
import backend.model.converter.UserConverter;
import backend.model.entity.User;
import backend.service.UserService;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * 用户注册
     * @param userLoginDTO 用户信息
     * @return token
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        List<String> fields = List.of("username", "password");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User selectUser = User.builder().username(userLoginDTO.getUsername()).build();
        User targetUser = userMapper.selectUser(selectUser, scope).getFirst();

        if(passwordEncoder.matches(userLoginDTO.getPassword(), targetUser.getPassword())){
            return UserLoginVO.builder().setToken(userLoginDTO.getUsername(), jwtService).build();
        }

        throw new LoginFailedException(HttpStatus.FORBIDDEN.value(), "用户名或密码错误");
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @Override
    public UserProfileVO getUser() {
        return UserConverter.INSTANCE.UserToUserProfileVO(User.getAuth());
    }
}
