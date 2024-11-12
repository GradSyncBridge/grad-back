package backend.service;

import backend.model.VO.deadline.DeadlineVO;
import java.util.List;

public interface DeadlineService {
    List<DeadlineVO> getDeadline();
}
