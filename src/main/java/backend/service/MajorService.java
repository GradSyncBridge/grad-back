package backend.service;

import backend.model.VO.major.MajorFirstVO;
import backend.model.VO.major.MajorSecondVO;
import backend.model.VO.major.MajorVO;

import java.util.List;

public interface MajorService {
    /**
     * 获取对应学院下的学科
     * /unauthorized/catalogue
     * @param department 学院
     * @return 一级学科列表
     */
    List<MajorVO> getCatalogue(Integer department);

    /**
     * 获取对应学院下的一级学科
     * GET /unauthorized/catalogue/first
     * @param department 学院ID
     * @return 一级学科列表
     */
    List<MajorFirstVO> getFirstMajorByDept(Integer department);

    /**
     * 获取对应一级学科下的二级学科
     * GET /unauthorized/catalogue/second
     * @param major 一级学科
     * @return 二级学科列表
     */
    List<MajorSecondVO> getSecondMajorByFirst(Integer major);
}
