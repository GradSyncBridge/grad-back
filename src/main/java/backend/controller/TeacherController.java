package backend.controller;

import backend.service.TeacherService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 获取教师个人信息
     * @param uid 教师uid
     * @return 教师个人信息
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> getTeacherProfile(@RequestParam(value = "uid") Integer uid){
        return ResultEntity.success(200, "Get Teacher's profile successfully.", teacherService.getTeacherProfile(uid));
    }
}
