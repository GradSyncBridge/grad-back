package backend.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.exception.model.user.UserRoleDeniedException;
import backend.mapper.LogMapper;
import backend.model.VO.log.LogVO;
import backend.model.converter.LogConverter;
import backend.model.entity.Log;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.service.LogService;
import backend.util.FieldsGenerator;

@Aspect
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogMapper logMapper;

    @Autowired
    LogConverter logConverter;

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

            return logConverter.LogListToLogVOList(logs);
        }catch (UserRoleDeniedException userRoleDeniedException){
            throw new UserRoleDeniedException(user.getRole(), 403, teacher == null ? null : teacher.getIdentity());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Around("execution(* backend.model.DTO.LogDTO.getthis(..))")
    public Object logging(ProceedingJoinPoint joinPoint) {
        Object result = null;

        try {
            result = joinPoint.proceed();
            try {
                Log log = (Log) result;
                logMapper.insertLog(log);
            }catch (Exception exception){
                exception.printStackTrace();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
