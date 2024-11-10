package backend.service.impl;

import backend.exception.model.User.UserNotFoundException;
import backend.exception.model.teacher.TeacherNotFoundException;
import backend.exception.model.teacher.TeacherProfileNoRightException;
import backend.mapper.MajorToTeacherMapper;
import backend.mapper.TeacherMapper;
import backend.mapper.UserMapper;
import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.model.VO.teacher.TeacherProfileVO;
import backend.model.VO.teacher.TeacherVO;
import backend.model.converter.TeacherConverter;
import backend.model.entity.MajorToTeacher;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.service.TeacherService;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 获取对应部门教师列表
     * @param department 部门
     * @return 教师列表
     */
    @Override
    public List<TeacherVO> getTeacher(Integer department) {
        List<Teacher> teachers =
                teacherMapper.selectTeacher(Teacher.builder().department(department).build(), FieldsGenerator.generateFields(Teacher.class));

        if(teachers.isEmpty()) throw new TeacherNotFoundException();

        List<User> users = getTeacherAsync(teachers).join();

        return teacherConverter.INSTANCE.TeacherListToTeacherVOList(teachers, users);
    }

    /**
     * 获取对应二级学科教师列表
     * @param majorID 二级学科
     * @return 教师列表
     */
    @Override
    public List<TeacherVO> getTeachersByCatalogue(Integer majorID) {

        List<MajorToTeacher> majorToTeachers = majorToTeacherMapper
                .selectMajorToTeacher(MajorToTeacher.builder().mid(majorID).build(),
                        FieldsGenerator.generateFields(MajorToTeacher.class));

        if(majorToTeachers.isEmpty()) throw new TeacherNotFoundException(majorID);

        List<Teacher> teachers = teacherMapper.selectTeacherForeach(majorToTeachers);

        List<User> users = getTeacherAsync(teachers).join();

        return teacherConverter.INSTANCE.TeacherListToTeacherVOList(teachers, users);
    }

    /**
     * 获取教师个人信息
     * @param uid 教师uid
     * @return 教师个人信息
     */
    @Override
    public TeacherProfileVO getTeacherProfile(Integer uid) {
        try{
            if(User.getAuth().getRole() != 2) throw new TeacherProfileNoRightException();

            User user = userMapper.selectUser(User.builder().id(uid).build(), FieldsGenerator.generateFields(User.class)).getFirst();

            Teacher teacher = teacherMapper.selectTeacher(Teacher.builder().userId(uid).build(), FieldsGenerator.generateFields(Teacher.class)).getFirst();

            return teacherConverter.INSTANCE.TeacherAndUserToTeacherProfileVO(teacher, user);
        }catch(TeacherProfileNoRightException teacherProfileNoRightException){
            throw new TeacherProfileNoRightException();
        }catch (Exception e){
            throw new UserNotFoundException();
        }
    }

    @Async
    public CompletableFuture<List<User>> getTeacherAsync(List<Teacher> teachers) {

        List<CompletableFuture<User>> futures = new ArrayList<>();

        try{

            for (Teacher interviewId : teachers) {
                CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return userMapper.selectUser(User.builder().id(interviewId.getId()).build(), FieldsGenerator.generateFields(User.class)).getFirst();
                    } catch (Exception e) {
                        throw new RuntimeException();
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

    @Override
    public void updateTeacherProfile(TeacherProfileUpdateDTO teacherProfile) {
        try {
            Teacher teacherUpdate = teacherConverter.TeacherProfileDTOToTeacher(teacherProfile);
            teacherMapper.updateTeacher(teacherUpdate, User.getAuth().getTeacher());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
