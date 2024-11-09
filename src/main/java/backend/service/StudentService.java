package backend.service;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentTableDTO;
import backend.model.VO.student.StudentSubmitTableVO;

public interface StudentService {
    void submitTable(StudentTableDTO studentTableDTO);

    StudentSubmitTableVO getStudentSubmitTable();

    void submitApplication(ApplicationSubmitDTO applicationSubmitDTO);
}
