package backend.controller;

import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.service.TeacherService;
import backend.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> alterTeacherProfile(@RequestBody @Validated TeacherProfileUpdateDTO teacherProfile) {
        teacherService.updateTeacherProfile(teacherProfile);
        return ResultEntity.success(200, "Modify teacher's profile successfully", null);
    }
}
