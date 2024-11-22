package backend.controller;

import backend.annotation.SysLog;
import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.service.AdminService;
import backend.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Admin identity verified in AdminFilter.
 * A Http.Forbidden value will be returned for
 * student users / teachers with low clearance.
 * */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @SysLog(value = "Put /admin/deadline - 管理员修改截止日期")
    @PutMapping(value = "/deadline")
    public ResponseEntity<ResultEntity<Object>> adminModifyDeadline(
            @RequestBody AdminDeadlineDTO deadlineDTO
    ) {
        adminService.adminModifyDeadline(deadlineDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Modify deadline successfully",
                null
        );
    }

    @SysLog(value = "Get /admin/teachers/remnant - 管理员获取教师剩余名额")
    @GetMapping(value = "/teachers/remnant")
    public ResponseEntity<ResultEntity<Object>> getTeachersWithMetric() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers with remaining metric",
                adminService.getTeachersWithMetric()
        );
    }

    @SysLog(value = "Get /admin/teachers - 管理员获取所有教师")
    @GetMapping(value = "/teachers")
    public ResponseEntity<ResultEntity<Object>> getAllTeachers() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers successfully",
                adminService.getAllTeachers()
        );
    }

    @SysLog(value = "Put /admin/teacher - 管理员修改教师信息")
    @PutMapping(value = "/teacher")
    public ResponseEntity<ResultEntity<Object>> adminModifyTeacher(
            @RequestBody @Validated AdminTeacherDTO adminTeacherDTO
    ) {
        adminService.adminModifyTeacher(adminTeacherDTO);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Modify teacher's profile successfully",
                null
        );
    }

    @SysLog(value = "Get /admin/teachers/empty - 管理员获取无学生的教师")
    @GetMapping(value = "/teachers/empty")
    public ResponseEntity<ResultEntity<Object>> getTeachersWithoutEnrolls() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers without enroll relations successfully",
                adminService.getTeachersWithoutEnrolls()
        );
    }

    @SysLog(value = "Get /admin/possible-enroll - 管理员获取可能录取结果")
    @PostMapping(value = "/possible-enroll")
    public ResponseEntity<ResultEntity<Object>> adminFilterPossibleEnrolls() {
        adminService.adminFilterPossibleEnrolls();

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Filter all possible enrolled students successfully",
                null
        );
    }

    @SysLog(value = "Get /admin/final-enroll - 管理员获取最终录取结果")
    @PostMapping(value = "/final-enroll")
    public ResponseEntity<ResultEntity<Object>> adminFilterFinalEnrolls() {
        adminService.adminFilterFinalEnrolls();

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Filter all final enrolled students successfully",
                null
        );
    }

    @SysLog(value = "Get /admin/filter-enroll - 管理员获取录取结果")
    @PostMapping(value = "/filter-enroll")
    public ResponseEntity<ResultEntity<Object>> adminFilterEnrolls(@RequestBody Map<String, Double> adminFilter) {
        adminService.adminFilterEnrolls(adminFilter.get("ratio"));

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Filter all students by first grade successfully",
                null
        );
    }

}
