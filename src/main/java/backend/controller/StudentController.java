package backend.controller;

import backend.model.DTO.StudentApplicationSubmitDTO;
import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.service.StudentService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getStudentSubmitTable(@RequestParam(value = "targetUid", defaultValue = "-1") Integer targetUid) {
        return ResultEntity.success(HttpStatus.OK.value(), "Get Student's submission table successfully", studentService.getStudentSubmitTable(targetUid));
    }

    // New Interfaces
    @GetMapping(value = "/search")
    public ResponseEntity<ResultEntity<Object>> searchStudent(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "valid", defaultValue = "0") Integer valid
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Search result displayed successfully",
                studentService.searchStudent(key, valid)
        );
    }

    @PostMapping
    public ResponseEntity<ResultEntity<Object>> submitStudent(@RequestBody @Validated StudentSubmitDTO submit) {
        studentService.studentSubmit(submit);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student table submitted successfully",
                null
        );
    }

    @PutMapping
    public ResponseEntity<ResultEntity<Object>> modifyStudent(@RequestBody @Validated StudentSubmitDTO submit) {
        studentService.studentSubmit(submit);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Update student submission table successfully",
                null
        );
    }

    @PostMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> submitStudentGrade(@RequestBody StudentGradeSubmitDTO studentGrade) {
        studentService.studentGradeSubmit(studentGrade);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student's grade submitted successfully",
                null
        );
    }

    @PutMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> modifyStudentGrade(@RequestBody StudentGradeModifyDTO studentGrade) {
        studentService.studentGradeModify(studentGrade);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student's grade modified successfully",
                null
        );
    }


    @PostMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> submitStudentApplication(
            @RequestBody StudentApplicationSubmitDTO studentApplication
    ) {
        studentService.studentApplicationSubmit(studentApplication);

        return ResultEntity.success(
          HttpStatus.OK.value(),
          "Student's application submitted successfully",
          null
        );
    }
}
