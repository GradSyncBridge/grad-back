package backend.model.converter;

import backend.model.VO.enroll.EnrollVO;
import backend.model.entity.Department;
import backend.model.entity.Major;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EnrollConverter {
    EnrollConverter INSTANCE = Mappers.getMapper(EnrollConverter.class);

    @Mapping(target = "student", source = "student.name")
    @Mapping(target = "teacher", source = "teacher.name")
    @Mapping(target = "gender", source = "student.gender")
    @Mapping(target = "departmentNum", source = "dept.did")
    @Mapping(target = "departmentName", source = "dept.name")
    @Mapping(target = "majorNum", source = "major.mid")
    @Mapping(target = "majorName", source = "major.name")
    @Mapping(target = "enrollmentID", source = "enrollmentID")
    EnrollVO MultiAttributesToEnrollVO(User student, User teacher, Department dept, Major major, Integer enrollmentID);
}
