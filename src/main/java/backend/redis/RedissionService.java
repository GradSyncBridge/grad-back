package backend.redis;

import backend.model.DTO.EnrollConfirmDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@Aspect
public class RedissionService {

    @Autowired
    private RedissonClient redissonClient;

    @Around("execution(* backend.service.impl.EnrollServiceImpl.enrollConfirm(backend.model.DTO.EnrollConfirmDTO)) && args(confirm)")
    public Object enrollLockedTransaction(ProceedingJoinPoint joinPoint, EnrollConfirmDTO confirm) throws Throwable {
        RLock lock = redissonClient.getLock("enroll-lock:" + confirm.getStudentID());

        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(10, 30, TimeUnit.SECONDS);

            if (lockAcquired)  return joinPoint.proceed();

            throw new RuntimeException("Failed to acquire lock");
        } finally {
            if (lockAcquired) lock.unlock();
        }
    }

}
