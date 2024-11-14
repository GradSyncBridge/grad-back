package backend.service.impl;

import backend.mapper.SubjectMapper;

import backend.model.VO.subject.SubjectVO;
import backend.model.entity.User;

import backend.service.SubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public List<SubjectVO> getSubjects(Integer department) {
        try {
            return subjectMapper
                    .selectSubjectWithDept(department, User.getAuth().getRole() == 1 ? 0 : 1);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
}
