package backend.service;

import backend.model.VO.enroll.EnrollVO;
import java.util.List;

public interface EnrollService {
    List<EnrollVO> getEnrollTable(Integer departmentId, Integer year);

}
