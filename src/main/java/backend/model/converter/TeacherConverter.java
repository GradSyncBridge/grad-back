package backend.model.converter;

import backend.model.VO.major.TeacherInMajorVO;
import backend.model.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeacherConverter {
    TeacherConverter INSTANCE = Mappers.getMapper(TeacherConverter.class);

    TeacherInMajorVO TeacherToTeacherVO(Teacher teacher);
}
