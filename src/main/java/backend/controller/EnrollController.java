package backend.controller;

import backend.model.DTO.EnrollConfirmDTO;
import backend.service.EnrollService;
import backend.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/enroll")
public class EnrollController {
    @Autowired
    private EnrollService enrollService;

    @PostMapping
    public ResponseEntity<ResultEntity<Object>> enrollConfirm(@RequestBody EnrollConfirmDTO confirmDTO) {
        enrollService.enrollConfirm(confirmDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Enroll relationship recorded successfully",
                null
        );
    }

    @DeleteMapping
    public ResponseEntity<ResultEntity<Object>> enrollCancel(@RequestBody Map<String, Integer> cancelDTO) {
        enrollService.enrollCancel(cancelDTO.get("enrollmentID"));
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Enroll relationship canceled successfully",
                null
        );
    }

    @GetMapping(value = "/list")
    public ResponseEntity<ResultEntity<Object>> enrollList() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "List all enroll relationships successfully",
                enrollService.getEnrollList()
        );
    }

}
