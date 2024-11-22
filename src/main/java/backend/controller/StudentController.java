package backend.controller;

import backend.annotation.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.model.DTO.StudentApplicationSubmitDTO;
import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.service.StudentService;
import backend.util.ResultEntity;

/**
 * StudentController
 *
 * @function getStudentSubmitTable 获取学生提交表
 * @function searchStudent 搜索学生
 * @function submitStudent 学生提交报名表单
 * @function modifyStudent 学生修改报名表单
 * @function submitStudentGrade 学生/教师提交成绩信息
 * @function modifyStudentGrade 学生/教师修改成绩信息
 *
 */
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取学生提交信息
     * GET /student
     * @param targetUid 目标用户ID
     * @return 学生提交表
     */
    @SysLog(value = "GET /student 获取学生提交信息")
    @GetMapping
    public ResponseEntity<ResultEntity<Object>> getStudentSubmitTable(
            @RequestParam(value = "targetUid", defaultValue = "-1") Integer targetUid
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get Student's submission table successfully",
                studentService.getStudentSubmitTable(targetUid));
    }

    /**
     * 搜索学生
     * GET /student/search
     * @param key 关键词
     * @param valid 用户是否有效
     * @return 学生列表
     */
    @SysLog(value = "GET /student/search 搜索学生")
    @GetMapping(value = "/search")
    public ResponseEntity<ResultEntity<Object>> searchStudent(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "valid", defaultValue = "0") Integer valid
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Search result displayed successfully",
                studentService.searchStudent(key, valid)
        );
    }

    /**
     * 学生提交报名表单
     * Post /student
     * @param submit 提交信息
     * @return 提交结果
     */
    @SysLog(value = "POST /student 学生提交报名表单")
    @PostMapping
    public ResponseEntity<ResultEntity<Object>> submitStudent(
            @RequestBody @Validated StudentSubmitDTO submit)
    {
        studentService.studentSubmit(submit);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student table submitted successfully",
                null
        );
    }

    /**
     * 学生修改报名表单
     * PUT /student
     * @param submit 提交信息
     * @return 提交结果
     */
    @SysLog(value = "PUT /student 学生修改报名表单")
    @PutMapping
    public ResponseEntity<ResultEntity<Object>> modifyStudent(
            @RequestBody @Validated StudentSubmitDTO submit)
    {
        studentService.studentSubmit(submit);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Update student submission table successfully",
                null
        );
    }

    /**
     * 学生/教师提交成绩信息
     * /student/grade
     * @param studentGrade 学生成绩
     * @return 提交结果
     */
    @SysLog(value = "POST /student/grade 学生/教师提交成绩信息")
    @PostMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> submitStudentGrade(
            @RequestBody StudentGradeSubmitDTO studentGrade)
    {
        studentService.studentGradeSubmit(studentGrade);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student's grade submitted successfully",
                null
        );
    }

    /**
     * 学生/教师修改成绩信息
     * PUT /student/grade
     * @param studentGrade 学生成绩
     * @return 提交结果
     */
    @SysLog(value = "PUT /student/grade 学生/教师修改成绩信息")
    @PutMapping(value = "/grade")
    public ResponseEntity<ResultEntity<Object>> modifyStudentGrade(
            @RequestBody StudentGradeModifyDTO studentGrade)
    {
        studentService.studentGradeModify(studentGrade);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Student's grade modified successfully",
                null
        );
    }

    /**
     * 学生提交志愿信息
     * POST /student/apply
     * @param studentApplication 学生志愿信息
     * @return 提交结果
     */
    @SysLog(value = "POST /student/apply - 学生提交志愿信息")
    @PostMapping(value = "/apply")
    public ResponseEntity<ResultEntity<Object>> submitStudentApplication(
            @RequestBody StudentApplicationSubmitDTO studentApplication
    ) {
        studentService.studentApplicationSubmit(studentApplication);

        return ResultEntity.success(
          HttpStatus.OK.value(),
          "Student's application submitted successfully",
          null
        );
    }
}
