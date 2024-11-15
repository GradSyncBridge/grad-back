package backend.controller;

import backend.service.SubjectService;

import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getSubject(@RequestParam(value = "departmentID", defaultValue = "0") Integer department) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get subjects successfully",
                subjectService.getSubjects(department)
        );
    }
}