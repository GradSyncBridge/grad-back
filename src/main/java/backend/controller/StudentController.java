package backend.controller;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.GradeSubmitListDTO;
import backend.model.DTO.StudentTableDTO;
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

    @PutMapping
    public ResponseEntity<ResultEntity<Object>> submitStudentTable(@RequestBody @Validated StudentTableDTO studentTableDTO) {
        studentService.submitTable(studentTableDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Update successfully.", null);
    }

    @PostMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> submitGrades(@RequestBody GradeSubmitListDTO gradeSubmitListDTO) {
        // studentService.submitGrade(User.getAuth().getId())
        return ResultEntity.success(HttpStatus.OK.value(), "Submit grade successfully.");
    }

    @PostMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> submitApplication(@RequestBody ApplicationSubmitDTO applicationSubmitDTO) {
        studentService.submitApplication(applicationSubmitDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Application info successfully recorded", null);
    }

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getStudentSubmitTable(){
        return ResultEntity.success(HttpStatus.OK.value(), "Get Student's submission table successfully", studentService.getStudentSubmitTable());
    }
}
