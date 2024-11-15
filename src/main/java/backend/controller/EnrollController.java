package backend.controller;

import backend.model.DTO.EnrollConfirmDTO;
import backend.service.EnrollService;
import backend.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * EnrollController
 *
 * @function enrollConfirm 导师确认录取学生
 * @function enrollCancel 导师取消录取关系
 * @function enrollList 导师查看已选学生信息
 */
@RestController
@RequestMapping(value = "/enroll")
public class EnrollController {
    @Autowired
    private EnrollService enrollService;

    /**
     * 导师确认录取学生
     * POST /enroll
     * @param confirmDTO 确认信息
     * @return null
     */
    @PostMapping
    public ResponseEntity<ResultEntity<Object>> enrollConfirm(
            @RequestBody EnrollConfirmDTO confirmDTO
    ) {
        enrollService.enrollConfirm(confirmDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Enroll relationship recorded successfully",
                null
        );
    }

    /**
     * 导师取消录取关系
     * DELETE /enroll
     * @param cancelDTO 取消信息
     * @return null
     */
    @DeleteMapping
    public ResponseEntity<ResultEntity<Object>> enrollCancel(@RequestBody Map<String, Integer> cancelDTO) {
        enrollService.enrollCancel(cancelDTO.get("enrollmentID"));

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Enroll relationship canceled successfully",
                null
        );
    }

    /**
     * 导师查看已选学生信息
     * GET /enroll/list
     * @return 已选学生列表
     */
    @GetMapping(value = "/list")
    public ResponseEntity<ResultEntity<Object>> enrollList() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "List all enroll relationships successfully",
                enrollService.getEnrollList()
        );
    }

}
