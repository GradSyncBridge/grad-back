package backend.controller;

import backend.service.DeadlineService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/deadline")
public class DeadlineController {
    @Autowired
    private DeadlineService deadlineService;

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getDeadline() {
        return ResultEntity.success(HttpStatus.OK.value(), "Get deadlines successfully", deadlineService.getDeadline());
    }
}
