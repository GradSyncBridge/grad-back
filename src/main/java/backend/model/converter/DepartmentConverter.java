package backend.model.converter;

import backend.model.VO.department.DepartmentVO;
import backend.model.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentConverter {
    DepartmentConverter INSTANCE = Mappers.getMapper(DepartmentConverter.class);

    @Mapping(target = "departmentID", source = "id")
    @Mapping(target = "departmentNum", source = "did")
    DepartmentVO DepartmentToDepartmentVO(Department department);

    List<DepartmentVO> DepartmentListTODepartmentVOList(List<Department> departmentList);
}
