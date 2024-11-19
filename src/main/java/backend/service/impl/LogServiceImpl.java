package backend.service.impl;

import java.util.logging.Logger;

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

//    @Override
//    public List<LogVO> getLog() {
//        User user = User.getAuth();
//        Teacher teacher = user.getTeacher();
//
//        List<String> fields = List.of("id", "userId", "endpoint", "operation", "created");
//        Map<String, Boolean> scope = FieldsGenerator.generateFields(Log.class, fields);
//        try{
//            if (teacher == null || teacher.getIdentity() != 3)
//                throw new UserRoleDeniedException();
//
//            List<Log> logs = logMapper.selectLog(Log.builder().build(), scope);
//
//            return logConverter.LogListToLogVOList(logs);
//        }catch (UserRoleDeniedException userRoleDeniedException){
//            throw new UserRoleDeniedException(user.getRole(), 403, teacher == null ? null : teacher.getIdentity());
//        }
//        catch (Exception e){
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    @Around("execution(* backend.model.DTO.LogDTO.getThis(..))")
    public void logging(ProceedingJoinPoint joinPoint) {
        System.out.println("hello");
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
