package backend.service;


import org.aspectj.lang.ProceedingJoinPoint;

public interface LogService {
    /**
     * 获取所有的日志信息
     * GET /log
     * @return 日志列表
    * */
    // List<LogVO> getLog();

    /**
     * 拦截getthis方法，插入日志信息
     * @param joinPoint 拦截方法 backend.model.LogDTO.getthis()方法
     * @return getthis方法返回内容
     * */

    // Object logging(ProceedingJoinPoint joinPoint);
}