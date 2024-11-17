package backend.service.impl;

import backend.mapper.LogMapper;
import backend.model.entity.Log;
import backend.model.entity.User;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

//@Aspect
//@Component
public class LogAspect {

//    @Autowired
//    LogMapper logMapper;
//
//    @After(value = "execution(* backend.service.UserService.*(..))")
//    public void logging(ProceedingJoinPoint joinPoint) {
//        User user = User.getAuth();
//
//        String interfaceName = joinPoint.getSignature().getDeclaringTypeName();
//        System.out.println(interfaceName);
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println(methodName);
//        Object[] args = joinPoint.getArgs();
//
//        try {
//
//            Log log = Log.builder().userId(user.getId()).endpoint(interfaceName + "." + methodName)
//                    .operation(Arrays.toString(args)).created(LocalDateTime.now())
//                    .build();
//
//            logMapper.insertLog(log);
//
//        }
//        catch (Exception e){
//            throw new RuntimeException(e.getMessage());
//        }
//    }
}
