package backend.service;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.model.DTO.MajorTeacherAddDTO;
import backend.model.DTO.MajorTeacherModifyDTO;
import backend.model.VO.majorToTeacher.MajorTeacherVO;
import backend.model.VO.teacher.TeacherProfileVO;

import java.util.List;

public interface AdminService {
    void adminModifyDeadline(AdminDeadlineDTO deadlineDTO);

    void adminModifyTeacher(AdminTeacherDTO teacherDTO);

    void adminFilterEnrolls(Double ratio);

    void adminFilterPossibleEnrolls();

    void adminFilterFinalEnrolls();

    void adminModifyMajorToTeacher(MajorTeacherModifyDTO majorTeacherModify);

    void adminAddMajorToTeacher(MajorTeacherAddDTO majorTeacherAdd);

    List<TeacherProfileVO> getTeachersWithMetric();

    List<TeacherProfileVO> getAllTeachers();

    List<TeacherProfileVO> getTeachersWithoutEnrolls();

    List<MajorTeacherVO> getMajorToTeacher(Integer teacher);
}
