package backend.controller;

import backend.service.DeadlineService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DeadlineController
 *
 * @function getDeadline 获取所有截止日期
 */
@RestController
@RequestMapping(value = "/deadline")
public class DeadlineController {
    @Autowired
    private DeadlineService deadlineService;

    /**
     * 获取所有截止日期
     * GET /deadline
     * @return
     */
    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getDeadline() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get deadlines successfully",
                deadlineService.getDeadline());
    }
}
