package backend.service;

import backend.model.VO.subject.SubjectVO;

import java.util.List;

public interface SubjectService {

    /**
     * 获取学科信息
     * GET /subject
     * @param department 学院
     * @return 学科信息
     */
    List<SubjectVO> getSubjects(Integer department);
}
