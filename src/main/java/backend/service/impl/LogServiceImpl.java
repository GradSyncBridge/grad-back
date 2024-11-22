package backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import backend.annotation.SysLog;
import backend.exception.model.user.UserRoleDeniedException;
import backend.model.VO.log.LogVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.util.FieldsGenerator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
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

    @Before("@annotation(sysLog)")
    public void logMethodExecution(JoinPoint joinPoint, SysLog sysLog){

        String endPoint = sysLog.value();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = signature.getParameterNames();

        Object[] args = joinPoint.getArgs();
        StringBuilder operation = new StringBuilder();

        for(int i= 0; i < args.length; i++)
            operation.append(parameterNames[i]).append(": ").append(args[i]).append(" ");

        try {
            logMapper.insertLog(
                    Log.builder().userId(User.getAuth().getId())
                            .endpoint(endPoint)
                            .operation(operation.toString())
                            .created(LocalDateTime.now())
                            .build()
            );
        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }

}
