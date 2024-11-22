package backend.service.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import backend.exception.model.user.UserRoleDeniedException;
import backend.model.VO.log.LogVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.util.FieldsGenerator;
import backend.util.GlobalLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.mapper.LogMapper;
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
     * @param joinPoint 拦截方法 backend.model.GlobalLogger.getthis()方法
     * @return getthis方法返回内容
     * */
    @Around("execution(* backend.util.GlobalLogger.getThis(..))")
    public void logging(ProceedingJoinPoint joinPoint) {
        try {
            GlobalLogger globalLogger = (GlobalLogger) joinPoint.proceed();
            logMapper.insertLog(
                    Log.builder().userId(globalLogger.getUserId())
                            .endpoint(globalLogger.getEndpoint())
                            .operation(globalLogger.getOperation())
                            .created(globalLogger.getCreated())
                            .build()
            );

        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }

}
