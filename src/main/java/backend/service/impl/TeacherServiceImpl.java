package backend.service.impl;

import backend.exception.model.teacher.TeacherNotFoundException;
import backend.mapper.TeacherMapper;
import backend.mapper.UserMapper;
import backend.model.VO.teacher.TeacherVO;
import backend.model.converter.TeacherConverter;
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

    @Override
    public List<TeacherVO> getTeacher(Integer department) {
        List<Teacher> teachers =
                teacherMapper.selectTeacher(Teacher.builder().department(department).build(), FieldsGenerator.generateFields(Teacher.class));

        if(teachers.isEmpty()) throw new TeacherNotFoundException();

        List<User> users = getTeacherAsync(teachers).join();

        return teacherConverter.INSTANCE.TeacherListToTeacherVOList(teachers, users);
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
}
