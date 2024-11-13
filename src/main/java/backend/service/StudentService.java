package backend.service;

import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.model.VO.student.StudentSubmitTableVO;
import backend.model.VO.user.UserProfileVO;

import java.util.List;

public interface StudentService {

    StudentSubmitTableVO getStudentSubmitTable(Integer targetUid);

    // Newer interfaces
    List<UserProfileVO> searchStudent(String key, Integer valid);

    void studentSubmit(StudentSubmitDTO submitDTO);

    void studentGradeSubmit(StudentGradeSubmitDTO submitDTO);

    void studentGradeModify(StudentGradeModifyDTO modifyDTO);
}
