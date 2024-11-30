package backend.service;


import backend.model.VO.log.PageLogVO;

public interface LogService {
    /**
     * 获取所有的日志信息
     * GET /log
     * @return 日志列表
    * */
     PageLogVO getLog(Integer pageIndex, Integer pageSize);


}