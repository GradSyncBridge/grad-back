package backend.service.impl;

import backend.exception.model.deadline.DeadlineNotFoundException;

import backend.exception.model.user.UserNotFoundException;
import backend.mapper.DeadlineMapper;
import backend.mapper.TeacherMapper;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.model.VO.teacher.TeacherProfileVO;

import backend.model.entity.Deadline;
import backend.model.entity.Teacher;
import backend.model.entity.User;

import backend.service.AdminService;
import backend.util.FieldsGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public void adminModifyDeadline(AdminDeadlineDTO deadlineDTO) {

        try {
            Deadline query = Deadline.builder().id(deadlineDTO.getDeadlineID()).build();

            List<Deadline> deadlines = deadlineMapper.selectDeadline(
                    query, FieldsGenerator.generateFields(Deadline.class)
            );

            if (deadlines.isEmpty())
                throw new DeadlineNotFoundException();

            Deadline ddl = deadlines.getFirst();
            ddl.setName(deadlineDTO.getName());
            ddl.setTime(LocalDateTime.parse(String.format("%sT00:00", deadlineDTO.getDeadline())));

            deadlineMapper.updateDeadline(ddl, query);

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(deadlineDTO.getDeadlineID(), 1);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TeacherProfileVO> getTeachersWithMetric() {
        try {
            return teacherMapper.selectTeacherWithMetric(
                    User.getAuth().getTeacher().getDepartment(),
                    1
            );
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TeacherProfileVO> getAllTeachers() {
        try {
            return teacherMapper.selectTeacherWithMetric(
                    User.getAuth().getTeacher().getDepartment(),
                    0
            );
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void adminModifyTeacher(AdminTeacherDTO teacherDTO) {
        try {
            List<Teacher> teachers = teacherMapper.selectTeacher(
                    Teacher.builder().userId(teacherDTO.getTeacherID()).build(),
                    FieldsGenerator.generateFields(Teacher.class)
            );

            if (teachers.isEmpty())
                throw new UserNotFoundException();

            Teacher targetTeacher = teachers.getFirst();

            targetTeacher.setTitle(teacherDTO.getTitle());
            targetTeacher.setIdentity(teacherDTO.getIdentity());
            targetTeacher.setTotal(teacherDTO.getTotal());

            teacherMapper.updateTeacher(
                    targetTeacher,
                    Teacher.builder().userId(teacherDTO.getTeacherID()).build()
            );

        } catch (UserNotFoundException userNotFoundException) {
            throw new UserNotFoundException(teacherDTO.getTeacherID(), 2);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
