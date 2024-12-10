package backend.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubjectConverter {

    SubjectConverter INSTANCE = Mappers.getMapper(SubjectConverter.class);
}
