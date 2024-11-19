package backend.model.converter;

import backend.model.VO.log.LogVO;
import backend.model.entity.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Mapper(componentModel = "spring")
public interface LogConverter {
    LogConverter INSTANCE = Mappers.getMapper(LogConverter.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "endpoint", target = "endpoint")
    @Mapping(source = "operation", target = "operation")
    @Mapping(source = "created", target = "created")
    LogVO logToLogVO(Log log);

    List<LogVO> logListToLogVOList(List<Log> logs);
    default String formatCreated(LocalDateTime created) {
        return created != null ? created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }
}