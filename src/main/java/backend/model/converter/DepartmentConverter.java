package backend.model.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.model.VO.department.DepartmentDetailVO;
import backend.model.VO.department.DepartmentVO;
import backend.model.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentConverter {
    DepartmentConverter INSTANCE = Mappers.getMapper(DepartmentConverter.class);

    @Mapping(target = "departmentID", source = "id")
    @Mapping(target = "departmentNum", source = "did")
    DepartmentVO DepartmentToDepartmentVO(Department department);

    List<DepartmentVO> DepartmentListTODepartmentVOList(List<Department> departmentList);

    @Mapping(target = "departmentID", source = "department.id")
    @Mapping(target = "departmentNum", source = "department.did")
    DepartmentDetailVO DepartmentTODepartmentDetailVO(Department department, Integer totalMajor);
}
