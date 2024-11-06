package backend.service.impl;

import backend.mapper.DepartmentMapper;
import backend.model.VO.department.DepartmentVO;
import backend.model.converter.DepartmentConverter;
import backend.model.entity.Department;
import backend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentConverter departmentConverter;

    /**
     * 获取所有部门
     * @return 所有部门
     */
    @Override
    public List<DepartmentVO> getDepartment() {
        try{
            List<Department> departmentList = departmentMapper.selectAllDepartments();

            return departmentConverter.INSTANCE.DepartmentListTODepartmentVOList(departmentList);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
