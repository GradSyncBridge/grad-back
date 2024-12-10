package backend.controller;

import backend.annotation.SysLog;
import backend.model.DTO.TeacherProfileUpdateDTO;
import backend.service.TeacherService;
import backend.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
     * GET /teacher/profile
     * @param uid 教师uid
     * @return 教师个人信息
     */
    @SysLog(value = "GET /teacher/profile - 获取教师个人信息")
    @GetMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> getTeacherProfile(
            @RequestParam(value = "uid") Integer uid
    ){
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get Teacher's profile successfully",
                teacherService.getTeacherProfile(uid)
        );
    }

    /**
     * 修改教师个人信息
     * PUT /teacher/profile
     * @param teacherProfile 教师个人信息
     * @return 修改结果
     */
    @SysLog(value = "PUT /teacher/profile - 修改教师个人信息")
    @PutMapping(value = "/profile")
    public ResponseEntity<ResultEntity<Object>> alterTeacherProfile(
            @RequestBody @Validated TeacherProfileUpdateDTO teacherProfile
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Modify teacher's profile successfully",
                teacherService.updateTeacherProfile(teacherProfile)
        );
    }

    /**
     * 获取第一/第二/第三志愿选择教师的学生信息
     * GET /teacher/apply
     * @param level 志愿等级
     * @return 学生信息
     */
    @SysLog(value = "GET /teacher/apply - 获取第一/第二/第三志愿选择教师的学生信息")
    @GetMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> getTeacherApplicationByLevel(
            @RequestParam(value = "level") Integer level
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get teacher application by level successfully",
                teacherService.getTeacherApplicationByLevel(level)
        );
    }
}
