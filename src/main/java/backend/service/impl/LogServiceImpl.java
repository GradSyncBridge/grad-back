package backend.service.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import backend.exception.model.user.UserRoleDeniedException;
import backend.model.VO.log.LogVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.util.FieldsGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.mapper.LogMapper;
import backend.model.DTO.LogDTO;
import backend.model.converter.LogConverter;
import backend.model.entity.Log;
import backend.service.LogService;

@Aspect
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    private final Logger logger = Logger.getLogger(LogServiceImpl.class.getName());

    @Autowired
    private LogConverter logConverter;

    @Override
    public List<LogVO> getLog() {
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        List<String> fields = List.of("id", "userId", "endpoint", "operation", "created");
        Map<String, Boolean> scope = FieldsGenerator.generateFields(Log.class, fields);
        try{
            if (teacher == null || teacher.getIdentity() != 3)
                throw new UserRoleDeniedException();

            List<Log> logs = logMapper.selectLog(Log.builder().build(), scope);

            return logConverter.logListToLogVOList(logs);
        }catch (UserRoleDeniedException userRoleDeniedException){
            throw new UserRoleDeniedException(user.getRole(), 403, teacher == null ? null : teacher.getIdentity());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 拦截getthis方法，插入日志信息
     * @param joinPoint 拦截方法 backend.model.LogDTO.getthis()方法
     * @return getthis方法返回内容
     * */
    @Around("execution(* backend.model.DTO.LogDTO.getThis(..))")
    public void logging(ProceedingJoinPoint joinPoint) {
        try {
            LogDTO logDTO = (LogDTO) joinPoint.proceed();
            logMapper.insertLog(
                    Log.builder().userId(logDTO.getUserId())
                            .endpoint(logDTO.getEndpoint())
                            .operation(logDTO.getOperation())
                            .created(logDTO.getCreated())
                            .build()
            );

        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }

}
