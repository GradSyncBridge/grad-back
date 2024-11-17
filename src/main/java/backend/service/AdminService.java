package backend.service;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.model.VO.teacher.TeacherProfileVO;

import java.util.List;

public interface AdminService {
    void adminModifyDeadline(AdminDeadlineDTO deadlineDTO);

    void adminModifyTeacher(AdminTeacherDTO teacherDTO);

    void adminFilterEnrolls(Double ratio);

    void adminFilterPossibleEnrolls();

    void adminFilterFinalEnrolls();

    List<TeacherProfileVO> getTeachersWithMetric();

    List<TeacherProfileVO> getAllTeachers();

    List<TeacherProfileVO> getTeachersWithoutEnrolls();
}
