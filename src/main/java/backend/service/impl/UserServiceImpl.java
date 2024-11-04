package backend.service.impl;

import backend.config.JwtService;
import backend.exception.model.user.DuplicateUserException;
import backend.exception.model.user.LoginFailedException;
import backend.mapper.UserMapper;
import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.UserLoginVO;
import backend.model.VO.UserProfileVO;
import backend.model.VO.UserRefreshVO;
import backend.model.VO.UserRegisterVO;
import backend.model.converter.UserConverter;
import backend.model.entity.User;
import backend.service.UserService;
import backend.util.FieldsGenerator;
import backend.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @param userLoginDTO 用户信息
     * @return token
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        List<String> fields = List.of("id", "username", "password");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User selectUser = User.builder().username(userLoginDTO.getUsername()).build();
        try {
            List<User> userList = userMapper.selectUser(selectUser, scope);

            if (userList.isEmpty() || !passwordEncoder.matches(userLoginDTO.getPassword(), userList.getFirst().getPassword()))
                throw new LoginFailedException();

            return UserLoginVO.builder().setToken(userList.getFirst().getId(), jwtService).build();
        } catch (LoginFailedException ex) {
            throw new LoginFailedException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public UserProfileVO getUser() {
        return UserConverter.INSTANCE.UserToUserProfileVO(User.getAuth());
    }

    /**
     * 刷新token
     *
     * @return token
     */
    @Override
    public UserRefreshVO refreshToken() {
        return UserRefreshVO.builder().setToken(User.getAuth().getId(), jwtService).build();
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户信息
     * @return token
     */
    @Override
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {
        User user = UserConverter.INSTANCE.UserRegisterDTOToUser(userRegisterDTO);
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            List<User> userList = userMapper.selectUser(User.builder().username(userRegisterDTO.getUsername()).build(), Map.of("username", true));
            if (!userList.isEmpty()) throw new DuplicateUserException();

            userMapper.insertUser(user);
        } catch (DuplicateUserException ex) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return UserRegisterVO.builder().setToken(user.getId(), jwtService).build();
    }

    /**
     * 更新用户信息
     *
     * @param userProfileUpdateDTO 用户信息
     * @return 用户信息
     */
    @Override
    public UserProfileUpdateDTO updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO) {

        try {
            List<User> userList = userMapper.selectUser(User.builder().username(userProfileUpdateDTO.getUsername()).build(), Map.of("username", true));

            if (!userList.isEmpty()) throw new DuplicateUserException();

            userProfileUpdateDTO.setAvatar(FileManager.saveBase64Image(userProfileUpdateDTO.getAvatar()));
            User user = UserConverter.INSTANCE.UserProfileUpdateDTOToUser(userProfileUpdateDTO);

            userMapper.updateUser(user, User.getAuth());
        } catch (DuplicateUserException duplicateUserException) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return userProfileUpdateDTO;
    }
}
