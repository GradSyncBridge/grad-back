package backend.service;


import backend.model.VO.log.LogVO;

import java.util.List;

public interface LogService {
    /**
     * 获取所有的日志信息
     * GET /log
     * @return 日志列表
    * */
     List<LogVO> getLog();


}