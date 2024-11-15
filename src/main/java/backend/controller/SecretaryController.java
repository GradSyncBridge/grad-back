package backend.controller;

import backend.model.DTO.SecretaryExamineDTO;
import backend.model.DTO.SecretaryGradeDTO;
import backend.service.SecretaryService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SecretaryController
 *
 * @function examineStudentSubmission 研究生管理秘书审核学生报名表单
 * @function modifyStudentGrade 研究生管理秘书修改初试成绩
 */
@RestController
@RequestMapping(value = "/secretary")
public class SecretaryController {
    @Autowired
    private SecretaryService secretaryService;

    /**
     * 研究生管理秘书审核学生报名表单
     * POST /secretary/examine
     * @param examineDTO 学生报名表单审核信息
     * @return void
     */
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

    /**
     * 研究生管理秘书修改初试成绩
     * PUT /secretary/grade
     * @param gradeDTO 学生成绩信息
     * @return void
     */
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
