package backend.service;

import java.util.List;

import backend.model.VO.department.DepartmentDeatilVO;
import backend.model.VO.department.DepartmentVO;

public interface DepartmentService {

    /**
     * 获取所有部门
     * @return 所有部门
     */
    List<DepartmentVO> getDepartment();

    DepartmentDeatilVO getDepartmentDetail(Integer departmentID);
}
