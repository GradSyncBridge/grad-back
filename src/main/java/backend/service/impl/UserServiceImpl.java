package backend.service.impl;

import backend.config.JwtService;
import backend.exception.model.user.DuplicateUserException;
import backend.exception.model.user.LoginFailedException;
import backend.mapper.StudentMapper;
import backend.mapper.TeacherMapper;
import backend.mapper.UserMapper;
import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.user.UserLoginVO;
import backend.model.VO.user.UserProfileVO;
import backend.model.VO.user.UserRefreshVO;
import backend.model.VO.user.UserRegisterVO;
import backend.model.converter.UserConverter;
import backend.model.entity.Student;
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

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 用户注册
     *
     * @param userLoginDTO 用户信息
     * @return token
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        List<String> fields = List.of("id", "username", "password", "role");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User selectUser = User.builder().username(userLoginDTO.getUsername()).build();
        try {
            List<User> userList = userMapper.selectUser(selectUser, scope);

            if (userList.isEmpty() || !passwordEncoder.matches(userLoginDTO.getPassword(), userList.getFirst().getPassword()))
                throw new LoginFailedException();

            return UserLoginVO.builder().setToken(userList.getFirst().getId(), userList.getFirst().getRole(), jwtService).build();
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
        return UserRefreshVO.builder().setToken(User.getAuth().getId(), User.getAuth().getRole(), jwtService).build();
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
            if(user.getRole() == 1) studentMapper.insertStudent(Student.builder().userId(user.getId()).valid(-1).disabled(1).build());
            else teacherMapper.insertTeacher(backend.model.entity.Teacher.builder().userId(user.getId()).build());
        } catch (DuplicateUserException ex) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            userMapper.deleteUser(User.builder().username(userRegisterDTO.getUsername()).build());
            throw new RuntimeException(e.getMessage());
        }

        return UserRegisterVO.builder().setToken(user.getId(), user.getRole(), jwtService).build();
    }

    /**
     * 更新用户信息
     *
     * @param userProfileUpdateDTO 用户信息
     * @return 用户信息
     */
    @Override
    public UserProfileUpdateDTO updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO) {

        String username = userProfileUpdateDTO.getUsername();
        try {
            if(!username.equals(User.getAuth().getUsername())) {
                List<User> userList = userMapper.selectUser(User.builder().username(userProfileUpdateDTO.getUsername()).build(), Map.of("username", true));

                if (!userList.isEmpty()) throw new DuplicateUserException();
            }else {
                userProfileUpdateDTO.setUsername(null);
            }

            userProfileUpdateDTO.setAvatar(FileManager.saveBase64Image(userProfileUpdateDTO.getAvatar()));
            User user = UserConverter.INSTANCE.UserProfileUpdateDTOToUser(userProfileUpdateDTO);

            userMapper.updateUser(user, User.getAuth());
        } catch (DuplicateUserException duplicateUserException) {
            throw new DuplicateUserException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        userProfileUpdateDTO.setUsername(username);
        return userProfileUpdateDTO;
    }
}
