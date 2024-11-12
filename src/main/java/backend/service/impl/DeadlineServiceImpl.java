package backend.service.impl;

import backend.mapper.DeadlineMapper;
import backend.model.VO.deadline.DeadlineVO;
import backend.model.converter.DeadlineConverter;
import backend.model.entity.Deadline;
import backend.service.DeadlineService;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeadlineServiceImpl implements DeadlineService {
    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private DeadlineConverter deadlineConverter;

    @Override
    public List<DeadlineVO> getDeadline() {
        return deadlineMapper
                .selectDeadline(Deadline.builder().build(), FieldsGenerator.generateFields(Deadline.class))
                .stream()
                .map(d -> deadlineConverter.INSTANCE.DeadlineToDeadlineVO(d, d.getTime().toString().split("T")[0]))
                .toList();
    }
}
