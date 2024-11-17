package backend.controller;


import backend.model.DTO.LogDTO;
import backend.service.LogService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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