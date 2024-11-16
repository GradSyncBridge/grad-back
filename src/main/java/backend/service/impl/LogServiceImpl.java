package backend.service.impl;

import backend.exception.model.user.UserRoleDeniedException;
import backend.mapper.LogMapper;
import backend.model.DTO.LogDTO;
import backend.model.VO.log.LogVO;
import backend.model.converter.LogConverter;
import backend.model.entity.Log;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.service.LogService;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public void logging(LogDTO logDTO) {
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();
        try {
            if (teacher == null || teacher.getIdentity() != 3)
                throw new UserRoleDeniedException();

            Log log = Log.builder().userId(user.getId()).endpoint(logDTO.getEndpoint())
                    .operation(logDTO.getOperation()).created(LocalDateTime.now())
                    .build();

            logMapper.insertLog(log);

        }catch (UserRoleDeniedException userRoleDeniedException){
            throw new UserRoleDeniedException(user.getRole(), 403, teacher == null ? null : teacher.getIdentity());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
