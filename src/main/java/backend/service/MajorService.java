package backend.service;

import backend.model.VO.major.MajorVO;

import java.util.List;

public interface MajorService {
    /**
     * 获取专业目录
     * @param department 学院
     * @return 专业列表
     */
    List<MajorVO> getCatalogue(Integer department);
}
