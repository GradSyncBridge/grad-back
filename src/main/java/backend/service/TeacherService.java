package backend.service;

import backend.model.VO.teacher.TeacherVO;

import java.util.List;

public interface TeacherService {
    List<TeacherVO> getTeacher(Integer department);

    List<TeacherVO> getTeachersByCatalogue(Integer majorID);
}
