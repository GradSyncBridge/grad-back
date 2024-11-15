package backend.controller;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.service.AdminService;
import backend.util.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping(value = "/teachers/remnant")
    public ResponseEntity<ResultEntity<Object>> getTeachersWithMetric() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers with remaining metric",
                adminService.getTeachersWithMetric()
        );
    }

    @GetMapping(value = "/teachers")
    public ResponseEntity<ResultEntity<Object>> getAllTeachers() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers successfully",
                adminService.getAllTeachers()
        );
    }

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

    @GetMapping(value = "/teachers/empty")
    public ResponseEntity<ResultEntity<Object>> getTeachersWithoutEnrolls() {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get all teachers without enroll relations successfully",
                adminService.getTeachersWithoutEnrolls()
        );
    }

    @PostMapping(value = "/possible-enroll")
    public ResponseEntity<ResultEntity<Object>> adminFilterPossibleEnrolls() {
        adminService.adminFilterPossibleEnrolls();

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Filter all possible enrolled students successfully",
                null
        );
    }

}
