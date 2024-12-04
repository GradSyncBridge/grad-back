package backend.controller;

import backend.model.DTO.NoticeCreateDTO;
import backend.service.NoticeService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ResultEntity<Object>> createNotice(@RequestBody @Validated NoticeCreateDTO noticeCreateDTO) {
        noticeService.createNotice(noticeCreateDTO);
        return ResultEntity.success(
                200,
                "Create notice successfully"
        );
    }

    @DeleteMapping
    private ResponseEntity<ResultEntity<Object>> deleteNotice(@RequestBody Map<String, Integer> map) {
        noticeService.deleteNotice(map.get("noticeID"));
        return ResultEntity.success(
                200,
                "Delete notice successfully"
        );
    }

    @GetMapping
    private ResponseEntity<ResultEntity<Object>> getNotice(
            @RequestParam Integer pageIndex,
            @RequestParam Integer pageSize,
            @RequestParam Integer publish
    ) {
        return ResultEntity.success(
                200,
                "Get notice successfully",
                noticeService.getNotice(pageIndex, pageSize, publish)
        );
    }
}
