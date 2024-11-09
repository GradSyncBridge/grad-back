package backend.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.mapper.DepartmentMapper;
import backend.mapper.MajorMapper;
import backend.model.VO.department.DepartmentDetailVO;
import backend.model.VO.department.DepartmentVO;
import backend.model.converter.DepartmentConverter;
import backend.model.entity.Department;
import backend.model.entity.Major;
import backend.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentConverter departmentConverter;

    @Autowired
    private MajorMapper majorMapper;

    /**
     * 获取所有部门
     * 
     * @return 所有部门
     */
    @Override
    public List<DepartmentVO> getDepartment() {
        try {
            List<Department> departmentList = departmentMapper.selectAllDepartments();

            return departmentConverter.INSTANCE.DepartmentListTODepartmentVOList(departmentList);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DepartmentDetailVO getDepartmentDetail(Integer departmentID) {
        try {

            Department department = departmentMapper.selectDepartmentDetail(departmentID);
            Integer totalMajor = majorMapper
                    .selectMajor(Major.builder().department(departmentID).build(), Map.of("id", true)).size();

            return departmentConverter.INSTANCE.DepartmentTODepartmentDetailVO(department, totalMajor);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
