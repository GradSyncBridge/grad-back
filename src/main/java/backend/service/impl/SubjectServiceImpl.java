package backend.service.impl;

import backend.mapper.SubjectMapper;

import backend.model.VO.subject.SubjectVO;
import backend.model.entity.User;

import backend.service.SubjectService;

import backend.util.GlobalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    /**
     * 获取学科信息
     * GET /subject
     * @param department 学院
     * @return 学科信息
     */
    @Override
    public List<SubjectVO> getSubjects(Integer department) {
        try {
            return subjectMapper
                    .selectSubjectWithDept(department, User.getAuth().getRole() == 1 ? 0 : 1);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            GlobalLogger.builder().userId(User.getAuth().getId()).created(LocalDateTime.now())
                    .endpoint("GET /subject").operation("department ID: " + department).build().getThis();
        }

    }
}
