package backend.service;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.model.VO.student.StudentSubmitTableVO;
import backend.model.VO.user.UserProfileVO;

import java.util.List;

public interface StudentService {

    StudentSubmitTableVO getStudentSubmitTable(Integer targetUid);

    void submitApplication(ApplicationSubmitDTO applicationSubmitDTO);

    void modifyApplication(ApplicationSubmitDTO applicationSubmitDTO);

    // Newer interfaces
    List<UserProfileVO> searchStudent(String key, Integer valid);

    void studentSubmit(StudentSubmitDTO submitDTO);
}
