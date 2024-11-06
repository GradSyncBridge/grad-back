package backend.controller;

import backend.model.DTO.StudentTableDTO;
import backend.service.StudentService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<ResultEntity<Object>> login(@RequestBody @Validated StudentTableDTO studentTableDTO) {
        studentService.submitTable(studentTableDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Login successfully.", null);
    }
}
