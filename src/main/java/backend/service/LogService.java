package backend.service;

import backend.model.DTO.LogDTO;
import backend.model.VO.log.LogVO;
import backend.model.entity.Log;

import java.util.List;

public interface LogService {

    List<LogVO> getLog();

    void logging(LogDTO logDTO);
}
