package backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.service.LogService;
import backend.util.ResultEntity;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getLog(){
        return ResultEntity.success(HttpStatus.OK.value(),
                "Get Logs information successfully", logService.getLog());
    }

}