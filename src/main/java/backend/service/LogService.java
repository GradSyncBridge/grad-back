package backend.service;

import backend.model.VO.log.LogVO;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;

public interface LogService {
    List<LogVO> getLog();

    Object logging(ProceedingJoinPoint joinPoint);
}