package backend.model.converter;

import backend.model.DTO.LogDTO;
import backend.model.VO.log.LogVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface LogConverter {

    LogConverter INSTANCE = Mappers.getMapper(LogConverter.class);

    default LogVO logDTOToLogVO(LogDTO log) {
        LogVO logVO = new LogVO();
        logVO.getUser().setUid(log.getUid());
        logVO.getUser().setUsername(log.getUsername());
        logVO.getUser().setAvatar(log.getAvatar());
        logVO.getUser().setName(log.getName());
        logVO.getUser().setEmail(log.getEmail());
        logVO.getUser().setGender(log.getGender());
        logVO.getUser().setPhone(log.getPhone());
        logVO.setEndpoint(log.getEndpoint());
        logVO.setCreated(log.getCreated());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logVO.setOperation(objectMapper.readValue(log.getOperation(), new TypeReference<Map<String,Object>>(){}));
        } catch (Exception e) {
            logVO.setOperation(log.getOperation());
        }

        return logVO;
    }

    default List<LogVO> logListToLogVOList(List<LogDTO> logs){

        List<LogVO> logVOS = new ArrayList<>();

        for (LogDTO log : logs) {
            logVOS.add(logDTOToLogVO(log));
        }

        return logVOS;
    }
}