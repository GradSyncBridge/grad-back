package backend.service.impl;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import backend.annotation.SysLog;
import backend.exception.model.user.UserRoleDeniedException;
import backend.model.DTO.LogDTO;
import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.model.DTO.UserProfileUpdateDTO;
import backend.model.VO.log.PageLogVO;
import backend.model.VO.log.LogVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

            PageHelper.startPage(pageIndex, pageSize, true);

            Page<LogDTO> page = logMapper.selectLog();

            List<LogVO> logVOS = logConverter.logListToLogVOList(page.getResult());

            return PageLogVO.builder().logList(logVOS).total((int) page.getTotal()).build();
        }catch (UserRoleDeniedException userRoleDeniedException){
            throw new UserRoleDeniedException(user.getRole(), 403, teacher == null ? null : teacher.getIdentity());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Before("@annotation(sysLog)")
    @Transactional
    public void logMethodExecution(JoinPoint joinPoint, SysLog sysLog){

        String endPoint = sysLog.value();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = signature.getParameterNames();

        Object[] args = joinPoint.getArgs();

        Map<String, Object> map = new HashMap<>();
        for(int i= 0; i < args.length; i++) {
            if(
                    args[i] instanceof Integer || args[i] instanceof String ||
                    args[i] instanceof Boolean || args[i] instanceof Long ||
                    args[i] instanceof Double  || args[i] instanceof Float ||
                    args[i] instanceof Short
            )
                map.put(parameterNames[i], args[i]);
            if(args[i] instanceof MultipartFile)
                map.put(parameterNames[i], ((MultipartFile) args[i]).getOriginalFilename());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String result, avatar = "";


            if(!map.isEmpty())
                result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            else{
                Method setAvatar = null;

                if(args[0] instanceof UserProfileUpdateDTO ||
                        args[0] instanceof User || args[0] instanceof TeacherProfileUpdateDTO
                ){
                    setAvatar = args[0].getClass().getDeclaredMethod("setAvatar", String.class);
                    Method getAvatar = args[0].getClass().getDeclaredMethod("getAvatar");
                    avatar = (String) getAvatar.invoke(args[0]);
                    setAvatar.invoke(args[0], User.getAuth().getAvatar());
                }

                result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(args[0]);

                if(setAvatar != null) setAvatar.invoke(args[0], avatar);
            }

            logMapper.insertLog(
                    Log.builder().userId(User.getAuth().getId())
                            .endpoint(endPoint)
                            .operation(result)
                            .created(LocalDateTime.now())
                            .build()
            );
        } catch (Throwable e) {
            logger.severe(e.getMessage());
        }
    }

}
