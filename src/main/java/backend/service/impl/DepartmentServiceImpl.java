package backend.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import backend.util.FieldsGenerator;
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
            return departmentConverter.INSTANCE
                    .DepartmentListTODepartmentVOList(departmentMapper.selectAllDepartments());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取学院详情
     * GET /unauthorized/department/detail
     * @param departmentID 学院ID
     * @return 学院详情
     */
    @Override
    public DepartmentDetailVO getDepartmentDetail(Integer departmentID) {
        try {
            CompletableFuture<Department> departmentFuture =
                    CompletableFuture.supplyAsync(()->
                            departmentMapper.selectDepartmentDetail(departmentID)
                    );

            CompletableFuture<Integer> totalMajorFuture =
                    CompletableFuture.supplyAsync(()->
                            majorMapper
                                    .selectMajor(Major.builder().department(departmentID).build(),
                                            FieldsGenerator.generateFields(Department.class))
                                    .size()
                    );

            return departmentConverter.INSTANCE.DepartmentTODepartmentDetailVO(departmentFuture.join(), totalMajorFuture.join());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
