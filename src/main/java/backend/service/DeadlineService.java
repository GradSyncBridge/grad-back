package backend.service;

import backend.model.VO.deadline.DeadlineVO;
import java.util.List;

public interface DeadlineService {

    /**
     * 获取所有截止日期
     * GET /deadline
     * @return 截止日期数组
     */
    List<DeadlineVO> getDeadline();
}
