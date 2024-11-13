package backend.service;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentTableDTO;
import backend.model.VO.student.StudentSubmitTableVO;
import backend.model.VO.user.UserProfileVO;

import java.util.List;

public interface StudentService {
    void submitTable(StudentTableDTO studentTableDTO);

    StudentSubmitTableVO getStudentSubmitTable(Integer targetUid);

    void submitApplication(ApplicationSubmitDTO applicationSubmitDTO);

    void modifyApplication(ApplicationSubmitDTO applicationSubmitDTO);

    List<UserProfileVO> searchStudent(String key, Integer valid);
}
