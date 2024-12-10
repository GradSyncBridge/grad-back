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

    /**
     * 创建公告
     * POST /notice
     * @param noticeCreateDTO 公告信息
     * @return null
     */
    @PostMapping
    public ResponseEntity<ResultEntity<Object>> createNotice(@RequestBody @Validated NoticeCreateDTO noticeCreateDTO) {
        noticeService.createNotice(noticeCreateDTO);
        return ResultEntity.success(
                200,
                "Create notice successfully",
                null
        );
    }

    /**
     * 删除公告
     * DELETE /notice
     * @param map 公告ID
     * @return null
     */
    @DeleteMapping
    private ResponseEntity<ResultEntity<Object>> deleteNotice(@RequestBody Map<String, Integer> map) {
        noticeService.deleteNotice(map.get("noticeID"));
        return ResultEntity.success(
                200,
                "Delete notice successfully",
                null
        );
    }

    /**
     * 获取公告
     * GET /notice
     * @param pageIndex 当前页
     * @param pageSize 每页数量
     * @param publish 是否发布
     * @return 公告列表
     */
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

    /**
     * 更新公告
     * PUT /notice
     * @param noticeCreateDTO 公告信息
     * @return null
     */
    @PutMapping
    private ResponseEntity<ResultEntity<Object>> updateNotice(@RequestBody @Validated NoticeCreateDTO noticeCreateDTO) {
        noticeService.updateNotice(noticeCreateDTO);
        return ResultEntity.success(
                200,
                "Update notice successfully",
                null
        );
    }

    /**
     * 获取公告详情
     * GET /notice/detail
     * @param noticeID 公告ID
     * @return 公告详情
     */
    @GetMapping(value = "/detail")
    private ResponseEntity<ResultEntity<Object>> getNoticeDetail(@RequestParam Integer noticeID) {
        return ResultEntity.success(
                200,
                "Get notice detail successfully",
                noticeService.getNoticeDetailByAdmin(noticeID)
        );
    }
}
