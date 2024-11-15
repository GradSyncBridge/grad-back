package backend.service;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.model.VO.teacher.TeacherProfileVO;

import java.util.List;

public interface AdminService {
    void adminModifyDeadline(AdminDeadlineDTO deadlineDTO);

    List<TeacherProfileVO> getTeachersWithMetric();

    List<TeacherProfileVO> getAllTeachers();

    void adminModifyTeacher(AdminTeacherDTO teacherDTO);

    List<TeacherProfileVO> getTeachersWithoutEnrolls();
}
