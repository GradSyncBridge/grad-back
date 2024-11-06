package backend.service;

import backend.model.VO.department.DepartmentVO;

import java.util.List;

public interface DepartmentService {

    /**
     * 获取所有部门
     * @return 所有部门
     */
    List<DepartmentVO> getDepartment();
}
