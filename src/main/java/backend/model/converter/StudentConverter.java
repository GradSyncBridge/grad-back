package backend.model.converter;

import backend.model.DTO.StudentTableDTO;
import backend.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentConverter {
    StudentConverter INSTANCE = Mappers.getMapper(StudentConverter.class);

    @Mapping(target = "examId", source = "studentTableDTO.examID")
    @Mapping(target = "certifyId", source = "studentTableDTO.certifyID")
    @Mapping(target = "majorStudy", source = "majorStudy")
    @Mapping(target = "birth", source = "studentTableDTO.birthday")
    Student StudentTableDTOToStudent(StudentTableDTO studentTableDTO, String majorStudy);
}


