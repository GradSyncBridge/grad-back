package backend.service;

import java.util.List;

import backend.model.VO.department.DepartmentDetailVO;
import backend.model.VO.department.DepartmentVO;

public interface DepartmentService {

    /**
     * 获取所有部门
     *
     * @return 所有部门
     */
    List<DepartmentVO> getDepartment();

    /**
     * 获取学院详情
     * GET /unauthorized/department/detail
     * @param departmentID 学院ID
     * @return 学院详情
     */
    DepartmentDetailVO getDepartmentDetail(Integer departmentID);
}
