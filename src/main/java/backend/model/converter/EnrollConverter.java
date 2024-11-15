package backend.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EnrollConverter {
    EnrollConverter INSTANCE = Mappers.getMapper(EnrollConverter.class);

}
