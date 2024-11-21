package backend.service.impl;

import backend.mapper.DeadlineMapper;
import backend.model.VO.deadline.DeadlineVO;
import backend.model.converter.DeadlineConverter;
import backend.model.entity.Deadline;
import backend.model.entity.User;
import backend.service.DeadlineService;
import backend.util.FieldsGenerator;
import backend.util.GlobalLogging;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DeadlineServiceImpl implements DeadlineService {
    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private DeadlineConverter deadlineConverter;

    /**
     * 获取所有截止日期
     * GET /deadline
     * @return 截止日期数组
     */
    @Override
    public List<DeadlineVO> getDeadline() {
        List<Deadline> deadlineList = deadlineMapper
                .selectDeadline(Deadline.builder().build(), FieldsGenerator.generateFields(Deadline.class));

        List<CompletableFuture<DeadlineVO>> deadlineVOListFuture = new ArrayList<>();
        for(Deadline d: deadlineList){
            CompletableFuture<DeadlineVO> deadlineVOCompletableFuture =
                    CompletableFuture.supplyAsync(()->
                                    deadlineConverter.INSTANCE.DeadlineToDeadlineVO(d, d.getTime().toString().split("T")[0])
                    );
            deadlineVOListFuture.add(deadlineVOCompletableFuture);
        }
        GlobalLogging.builder().userId(User.getAuth().getId()).created(LocalDateTime.now())
                .endpoint("GET /deadline").operation("null").build().getThis();

        return deadlineVOListFuture.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
