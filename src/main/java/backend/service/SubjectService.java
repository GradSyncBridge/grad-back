package backend.service;

import backend.model.VO.subject.SubjectVO;

import java.util.List;

public interface SubjectService {

    List<SubjectVO> getSubjects(Integer department);
}
