package backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import backend.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import backend.exception.model.user.UserRoleDeniedException;
import backend.exception.model.user.DuplicateUserEmailException;
import backend.exception.model.user.DuplicateUserException;
import backend.exception.model.teacher.TeacherNotFoundException;
import backend.exception.model.user.UserNotFoundException;

import backend.mapper.MajorToTeacherMapper;
import backend.mapper.TeacherMapper;
import backend.mapper.UserMapper;
import backend.mapper.StudentApplyMapper;

import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.model.VO.user.UserProfileVO;
import backend.model.VO.teacher.TeacherProfileVO;
import backend.model.VO.teacher.TeacherVO;

import backend.model.converter.UserConverter;
import backend.model.converter.TeacherConverter;

import backend.model.entity.StudentApply;
import backend.model.entity.MajorToTeacher;
import backend.model.entity.Teacher;
import backend.model.entity.User;

import backend.service.TeacherService;

import backend.util.FieldsGenerator;
import backend.util.FileManager;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeacherConverter teacherConverter;

    @Autowired
    private MajorToTeacherMapper majorToTeacherMapper;

    @Autowired
    private StudentApplyMapper studentApplyMapper;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private RedisService redisService;

    @Autowired
    private FileManager fileManager;

    /**
     * 获取学院下属所有老师
     * GET /unauthorized/department/teachers
     * @param department 学院
     * @return 老师列表
     */
    @Override
    public List<TeacherVO> getTeacher(Integer department) {
        List<TeacherVO> teacherVOS = (List<TeacherVO>) redisService.getData("teachersByDepartment:" + department);

        if(teacherVOS != null) return teacherVOS;

        List<Teacher> teachers = teacherMapper.selectTeacher(
                Teacher.builder().department(department).build(),
                FieldsGenerator.generateFields(Teacher.class)
        );

        if (teachers.isEmpty())
            return new ArrayList<>();

        List<User> users = getTeacherAsync(teachers).join();

        teacherVOS = teacherConverter.INSTANCE.TeacherListToTeacherVOList(teachers, users);

        redisService.saveDataWithExpiration("teachersByDepartment:" + department, 30, teacherVOS);

        return teacherVOS;
    }

    /**
     * 获取对应二级学科教师列表
     * GET /unauthorized/catalogue/teachers
     * @param majorID 二级学科
     * @return 教师列表
     */
    @Override
    public List<TeacherVO> getTeachersByCatalogue(Integer majorID) {

        List<TeacherVO> teacherVOS = (List<TeacherVO>) redisService.getData("teachersByCatalogue:" + majorID);

        if(teacherVOS != null) return teacherVOS;

        List<MajorToTeacher> majorToTeachers = majorToTeacherMapper.selectMajorToTeacher(
                MajorToTeacher.builder().mid(majorID).build(),
                FieldsGenerator.generateFields(MajorToTeacher.class)
        );

        if (majorToTeachers.isEmpty())
            throw new TeacherNotFoundException(majorID);

        List<Teacher> teachers = teacherMapper.selectTeacherForeach(majorToTeachers);
        List<User> users = getTeacherAsync(teachers).join();

        teacherVOS = teacherConverter.INSTANCE.TeacherListToTeacherVOList(teachers, users);

        redisService.saveDataWithExpiration("teachersByCatalogue:" + majorID, 30, teacherVOS);

        return teacherVOS;
    }

    /**
     * 获取教师个人信息
     * GET /teacher/profile
     * @param uid 教师uid
     * @return 教师个人信息
     */
    @Override
    public TeacherProfileVO getTeacherProfile(Integer uid) {
        try {
            if (User.getAuth().getRole() != 2)
                throw new UserRoleDeniedException();

            User user = userMapper.selectUser(
                    User.builder().id(uid).build(),
                    FieldsGenerator.generateFields(User.class)
            ).getFirst();

            Teacher teacher = teacherMapper.selectTeacher(
                    Teacher.builder().userId(uid).build(),
                    FieldsGenerator.generateFields(Teacher.class)
            ).getFirst();

            return teacherConverter.INSTANCE.TeacherAndUserToTeacherProfileVO(teacher, user);

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(1, 403);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    /**
     * 修改教师个人信息
     * PUT /teacher/profile
     * @param teacherProfile 教师个人信息
     */
    @Override
    @Transactional
    public TeacherProfileUpdateDTO updateTeacherProfile(TeacherProfileUpdateDTO teacherProfile) {
        try {
            User targetUser = User.getAuth();
            targetUser.setGender(teacherProfile.getGender());
            targetUser.setName(teacherProfile.getName());
            targetUser.setPhone(teacherProfile.getPhone());
            Teacher targetTeacher = targetUser.getTeacher();

            CompletableFuture<Void> usernameFuture = CompletableFuture.runAsync(()-> {
                String username = teacherProfile.getUsername();
                List<User> possibleUsers;
                if (!username.equals(targetUser.getUsername())) {
                    possibleUsers = userMapper.selectUser(
                            User.builder().username(username).build(),
                            FieldsGenerator.generateFields(User.class)
                    );

                    if (!possibleUsers.isEmpty())
                        throw new DuplicateUserException();
                    targetUser.setUsername(username);
                }
            });

            CompletableFuture<Void> emailFuture = CompletableFuture.runAsync(()-> {
                List<User> possibleUsers;
                String email = teacherProfile.getEmail();
                if (!email.equals(targetUser.getEmail())) {
                    possibleUsers = userMapper.selectUser(
                            User.builder().email(email).build(),
                            FieldsGenerator.generateFields(User.class)
                    );

                    if (!possibleUsers.isEmpty())
                        throw new DuplicateUserEmailException();
                    targetUser.setEmail(email);
                }
            });

            CompletableFuture<Void> avatarFuture = CompletableFuture.runAsync(()->{
                if (teacherProfile.getAvatar() != null) {
                    String avatar = targetUser.getAvatar();
                    if (avatar != null && !avatar.isEmpty()) {
                        CompletableFuture.runAsync(() ->
                                fileManager.deleteFile(avatar)
                        );
                        targetUser.setAvatar(fileManager.uploadBase64Image(avatar, targetUser));
                    }
                } else
                    targetUser.setAvatar(targetUser.getAvatar());
            });

            CompletableFuture.allOf(usernameFuture, emailFuture, avatarFuture).join();

            targetTeacher.setDescription(teacherProfile.getDescription());

            userMapper.updateUser(targetUser,
                        User.builder().id(targetUser.getId()).build());

            teacherMapper.updateTeacher(targetTeacher,
                    Teacher.builder().userId(targetTeacher.getUserId()).build());

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

        Teacher resultTeacher = teacherMapper.selectTeacher(
                Teacher.builder()
                        .userId(User.getAuth().getId())
                        .build(),
                FieldsGenerator.generateFields(Teacher.class)
        ).getFirst();

        teacherProfile.setUsername(resultUser.getUsername());
        teacherProfile.setEmail(resultUser.getEmail());
        teacherProfile.setAvatar(resultUser.getAvatar());
        teacherProfile.setDescription(resultTeacher.getDescription());
        teacherProfile.setGender(resultUser.getGender());
        teacherProfile.setName(resultUser.getName());
        teacherProfile.setPhone(resultUser.getPhone());

        return teacherProfile;
    }

    /**
     * 获取第一/第二/第三志愿选择教师的学生信息
     * GET /teacher/apply
     * @param level 志愿等级
     * @return 学生信息
     */
    @Override
    public List<UserProfileVO> getTeacherApplicationByLevel(Integer level) {
        User user = User.getAuth();
        Integer role = user.getRole();

        try {
            if (role == 1)
                throw new UserRoleDeniedException();

            List<CompletableFuture<UserProfileVO>> futures = new ArrayList<>();

            for(StudentApply s: studentApplyMapper
                    .selectStudentApply(
                            StudentApply.builder().tid(user.getId()).level(level).build(),
                            Map.of("userId", true)
                    )){

                CompletableFuture<UserProfileVO> future = CompletableFuture.supplyAsync(() -> {
                    List<User> users = userMapper.selectUser(
                            User.builder().id(s.getUserId()).build(),
                            FieldsGenerator.generateFields(User.class)
                    );
                    return users.isEmpty() ? null : userConverter.INSTANCE.UserToUserProfileVO(users.getFirst());
                });

                futures.add(future);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(role, 403);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public CompletableFuture<List<User>> getTeacherAsync(List<Teacher> teachers) {

        List<CompletableFuture<User>> futures = new ArrayList<>();

        try {
            for (Teacher teacher : teachers) {
                CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return userMapper.selectUser(User.builder().id(teacher.getUserId()).build(), FieldsGenerator.generateFields(User.class)).getFirst();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();

            List<User> allSubjects = new ArrayList<>();
            for (CompletableFuture<User> future : futures) {
                User subject = future.get();
                if (subject != null) allSubjects.add(subject);
            }

            return CompletableFuture.completedFuture(allSubjects);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
