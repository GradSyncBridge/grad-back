package backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import backend.annotation.SysLog;
import backend.exception.model.user.UserRoleDeniedException;
import backend.model.VO.log.PageLogVO;
import backend.model.VO.log.LogVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.util.FieldsGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public PageLogVO getLog(Integer pageIndex, Integer pageSize) {
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try{
            if (teacher == null || teacher.getIdentity() != 3)
                throw new UserRoleDeniedException();

            Page<Object> page = PageHelper.startPage(pageIndex, pageSize, true);

            List<LogVO> logVOS = logConverter.logListToLogVOList(
                    logMapper.selectLog(Log.builder().build(), FieldsGenerator.generateFields(Log.class))
            );

            return PageLogVO.builder().logs(logVOS).total((int) page.getTotal()).build();
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
                            .operation(operation.toString().length() > 255 ? operation.substring(0, 255) : operation.toString())
                            .created(LocalDateTime.now())
                            .build()
            );
        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }

}
