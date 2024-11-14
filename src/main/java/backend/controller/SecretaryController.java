package backend.controller;

import backend.model.DTO.SecretaryExamineDTO;
import backend.model.DTO.SecretaryGradeDTO;
import backend.service.SecretaryService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/secretary")
public class SecretaryController {
    @Autowired
    private SecretaryService secretaryService;

    @PostMapping(value = "/examine")
    public ResponseEntity<ResultEntity<Object>> examineStudentSubmission(
            @RequestBody SecretaryExamineDTO examineDTO
    ) {
        secretaryService.examineStudentSubmission(examineDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Examination result submitted successfully",
                null
        );
    }

    @PutMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> modifyStudentGrade(
            @RequestBody SecretaryGradeDTO gradeDTO
    ) {
        secretaryService.modifyStudentGrade(gradeDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student's grade modified successfully",
                null
        );
    }
}
