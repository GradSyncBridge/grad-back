package backend.controller;

import backend.service.MajorService;
import backend.util.ResultEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/unauthorized/major")
public class MajorController {
    @Autowired
    private MajorService majorService;

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> grabMajors(@Param(value = "department") Integer department) {
        return ResultEntity.success(200, "Get all majors successfully", majorService.grabMajors(department));
    }
}
