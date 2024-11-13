package backend.controller;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.GradeSubmitListDTO;
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

    @PostMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> submitGrades(@RequestBody GradeSubmitListDTO gradeSubmitListDTO) {
        return ResultEntity.success(HttpStatus.OK.value(), "Submit grade successfully.");
    }

    @PostMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> submitApplication(@RequestBody ApplicationSubmitDTO applicationSubmitDTO) {
        studentService.submitApplication(applicationSubmitDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Application info successfully recorded", null);
    }

    @PutMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> modifyApplication(@RequestBody ApplicationSubmitDTO applicationSubmitDTO) {
        studentService.modifyApplication(applicationSubmitDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Application info successfully modified", null);
    }

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
        return ResultEntity.success(HttpStatus.OK.value(), "Student table submitted successfully", null);
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
}
