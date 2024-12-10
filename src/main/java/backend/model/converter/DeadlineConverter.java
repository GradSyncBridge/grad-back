package backend.model.converter;

import backend.model.VO.deadline.DeadlineVO;
import backend.model.entity.Deadline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeadlineConverter {
    DeadlineConverter INSTANCE = Mappers.getMapper(DeadlineConverter.class);

    @Mapping(target = "DeadlineID", source = "deadline.id")
    @Mapping(target = "name", source = "deadline.name")
    @Mapping(target = "deadline", source = "time")
    DeadlineVO DeadlineToDeadlineVO(Deadline deadline, String time);
}
