package backend.service;

import backend.model.DTO.EnrollConfirmDTO;
import backend.model.VO.enroll.EnrollSelectVO;
import backend.model.VO.enroll.EnrollVO;
import java.util.List;

public interface EnrollService {
    List<EnrollVO> getEnrollTable(Integer departmentId, Integer year);

    void enrollConfirm(EnrollConfirmDTO confirmDTO);

    void enrollCancel(Integer enrollmentID);

    List<EnrollSelectVO> getEnrollList();
}
