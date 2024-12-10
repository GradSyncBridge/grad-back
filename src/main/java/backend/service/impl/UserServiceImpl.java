package backend.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backend.model.entity.Teacher;
import backend.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import backend.config.JwtService;
import backend.exception.model.user.DuplicateUserEmailException;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private FileManager fileManager;

    /**
     * 处理登录请求
     * POST /unauthorized/user/login
     *
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        Pattern pattern = Pattern.compile(GlobalConfig.emailPattern);
        Matcher matcher = pattern.matcher(userLoginDTO.getUsername());

        User selectUser;
        List<String> fields;

        if (matcher.matches()) {
            fields = List.of("id", "username", "password", "email", "role");
            selectUser = User.builder().email(userLoginDTO.getUsername()).build();
        } else {
            fields = List.of("id", "username", "password", "role");
            selectUser = User.builder().username(userLoginDTO.getUsername()).build();
        }

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);
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
     * GET /user/profile
     *
     * @return 用户信息
     */
    @Override
    public UserProfileVO getUser() {
        return UserConverter.INSTANCE.UserToUserProfileVO(User.getAuth());
    }

    /**
     * 刷新token
     * GET /user/refresh
     *
     * @return token
     */
    @Override
    public UserRefreshVO refreshToken() {
        User user = User.getAuth();
        return UserRefreshVO.builder().setToken(user.getId(), user.getRole(), jwtService).build();
    }

    /**
     * 处理注册请求
     * POST /unauthorized/user/register
     *
     * @param userRegisterDTO 注册信息
     * @return token
     */
    @Override
    @Transactional
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {
        User user = UserConverter.INSTANCE.UserRegisterDTOToUser(userRegisterDTO);
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            List<User> userList = userMapper.selectUser(User.builder().username(userRegisterDTO.getUsername()).build(), Map.of("username", true));
            if (!userList.isEmpty()) throw new DuplicateUserException();

            userMapper.insertUser(user);

            if (user.getRole() == 1)
                studentMapper.insertStudent(Student.builder().userId(user.getId()).valid(-1).disabled(1).build());
            else
                teacherMapper.insertTeacher(
                        Teacher.builder().userId(user.getId())
                                .department(userRegisterDTO.getDepartment())
                                .title(2).build()
                );
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
     * PUT /user/profile
     *
     * @param userProfileUpdateDTO 用户信息
     * @return 用户信息
     */
    @Override
    @Transactional
    public UserProfileUpdateDTO updateUserProfile(UserProfileUpdateDTO userProfileUpdateDTO) {

        String username = userProfileUpdateDTO.getUsername();
        String email = userProfileUpdateDTO.getEmail();
        try {
            User authUser = User.getAuth();

            CompletableFuture<Void> usernameFuture = CompletableFuture.supplyAsync(() -> {
                if (!username.equals(authUser.getUsername())) {
                    List<User> userList = userMapper.selectUser(
                            User.builder().username(userProfileUpdateDTO.getUsername()).build(),
                            Map.of("username", true));

                    if (!userList.isEmpty())
                        throw new DuplicateUserException();

                } else {
                    userProfileUpdateDTO.setUsername(null);
                }
                return userProfileUpdateDTO;
            }).thenAccept(dto -> userProfileUpdateDTO.setUsername(dto.getUsername()));

            CompletableFuture<Void> userEmailFuture = CompletableFuture.supplyAsync(() -> {
                if (!email.equals(authUser.getEmail())) {
                    List<User> userList = userMapper.selectUser(
                            User.builder().email(email).build(),
                            Map.of("email", true));

                    if (!userList.isEmpty())
                        throw new DuplicateUserEmailException();

                } else {
                    userProfileUpdateDTO.setEmail(null);
                }
                return userProfileUpdateDTO;
            }).thenAccept(dto -> userProfileUpdateDTO.setUsername(dto.getUsername()));

            CompletableFuture<Void> avatarFuture = CompletableFuture.supplyAsync(() -> {
                if (userProfileUpdateDTO.getAvatar() != null) {
                    String avatar = authUser.getAvatar();
                    if (avatar != null && !avatar.isEmpty()) {
                        if(!authUser.getAvatar().equals("default.png"))
                            CompletableFuture.runAsync(() ->
                                    fileManager.deleteFile(avatar)
                            );
                        userProfileUpdateDTO.setAvatar(
                                fileManager.uploadBase64Image(userProfileUpdateDTO.getAvatar(), authUser)
                        );
                    }
                }
                else
                    userProfileUpdateDTO.setAvatar(User.getAuth().getAvatar());

                return userProfileUpdateDTO;
            }).thenAccept(dto -> userProfileUpdateDTO.setAvatar(dto.getAvatar()));

            CompletableFuture.allOf(usernameFuture, userEmailFuture, avatarFuture).join();
            User user = UserConverter.INSTANCE.UserProfileUpdateDTOToUser(userProfileUpdateDTO);

            userMapper.updateUser(user, User.builder().id(authUser.getId()).build());

        } catch (DuplicateUserException duplicateUserException) {
            throw new DuplicateUserException();
        } catch (DuplicateUserEmailException duplicateUserEmailException) {
            throw new DuplicateUserEmailException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        User resultUser = userMapper.selectUser(
                User.builder()
                        .id(User.getAuth().getId())
                        .build(),
                FieldsGenerator.generateFields(User.class)
        ).getFirst();

        userProfileUpdateDTO.setUid(resultUser.getId());
        userProfileUpdateDTO.setUsername(resultUser.getUsername());
        userProfileUpdateDTO.setEmail(resultUser.getEmail());
        userProfileUpdateDTO.setAvatar(resultUser.getAvatar());
        userProfileUpdateDTO.setGender(resultUser.getGender());
        userProfileUpdateDTO.setName(resultUser.getName());
        userProfileUpdateDTO.setPhone(resultUser.getPhone());

        return userProfileUpdateDTO;
    }
}
