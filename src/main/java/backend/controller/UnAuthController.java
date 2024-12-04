package backend.controller;

import backend.annotation.SysLog;
import backend.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.user.UserLoginVO;
import backend.model.VO.user.UserRegisterVO;
import backend.util.ResultEntity;

/**
 * UnAuthController
 *
 * @function 处理与身份验证无关的请求
 * @function login 处理登录请求
 * @function register 处理注册请求
 * @function getCatalogue 获取所有专业
 * @function getFirstMajorByDept 获取对应学院下的一级学科
 * @function getSecondMajorByFirst 获取对应一级学科下的二级学科
 * @function getTeachersByCatalogue 获取对应二级学科教师列表
 * @function getTeacher 获取学院下属所有老师
 * @function getDepartmentDetail 获取学院详情
 * @function getDepartment 获取所有学院
 * @function getEnrollTable 获取学院录取学生
 */
@RestController
@RequestMapping(value = "/unauthorized")
public class UnAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private EnrollService enrollService;

    @Autowired
    private NoticeService noticeService;

    /**
     * 处理登录请求
     * POST /unauthorized/user/login
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping(value = "/user/login")
    public ResponseEntity<ResultEntity<Object>> login(
            @RequestBody @Validated UserLoginDTO userLoginDTO
    ) {
        UserLoginVO data = userService.login(userLoginDTO);
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Login successfully.",
                data);
    }

    /**
     * 处理注册请求
     * POST /unauthorized/user/register
     * @param userRegisterDTO 注册信息
     * @return token
     */
    @PostMapping(value = "/user/register")
    public ResponseEntity<ResultEntity<Object>> register(
            @RequestBody @Validated UserRegisterDTO userRegisterDTO
    ) {
        UserRegisterVO data = userService.register(userRegisterDTO);
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Register successfully.",
                data);
    }

    /**
     * 获取所有专业 To be deprecated
     * GET /unauthorized/major
     * @param department 学院
     * @return 专业列表
     */
    @GetMapping(value = "/catalogue")
    public ResponseEntity<ResultEntity<Object>> getCatalogue(
            @Param(value = "department") Integer department
    ) {
        return ResultEntity.success(
                200,
                "Get all majors successfully",
                majorService.getCatalogue(department));
    }

    /**
     * 获取对应学院下的一级学科
     * GET /unauthorized/catalogue/first
     * @param department 学院
     * @return 一级学科列表
     */
    @GetMapping(value = "/catalogue/first")
    public ResponseEntity<ResultEntity<Object>> getFirstMajorByDept(
            @RequestParam(value = "departmentID") Integer department
    ) {
        return ResultEntity.success(
            HttpStatus.OK.value(),
            "Get first majors successfully",
            majorService.getFirstMajorByDept(department)
        );
    }

    /**
     * 获取对应一级学科下的二级学科
     * GET /unauthorized/catalogue/second
     * @param majorID 一级学科
     * @return 二级学科列表
     */
    @GetMapping(value = "/catalogue/second")
    public ResponseEntity<ResultEntity<Object>> getSecondMajorByFirst(
            @RequestParam(value = "majorID") Integer majorID
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get second majors successfully",
                majorService.getSecondMajorByFirst(majorID)
        );
    }

    /**
     * 获取对应二级学科教师列表
     * GET /unauthorized/catalogue/teachers
     * @param majorID 二级学科
     * @return 教师列表
     */
    @GetMapping(value = "/catalogue/teachers")
    public ResponseEntity<ResultEntity<Object>> getTeachersByCatalogue(
            @RequestParam(value = "majorID") Integer majorID
    ) {
        return ResultEntity.success(
                200,
                "Get all departments successfully",
                teacherService.getTeachersByCatalogue(majorID));
    }

    /**
     * 获取学院下属所有老师
     * GET /unauthorized/department/teachers
     * @param department 学院
     * @return 老师列表
     */
    @GetMapping(value = "/department/teachers")
    public ResponseEntity<ResultEntity<Object>> getTeacher(
            @RequestParam(value = "departmentID") Integer department
    ) {
        return ResultEntity.success(
                200,
                "Get all teachers successfully",
                teacherService.getTeacher(department));
    }

    /**
     * 获取学院详情
     * GET /unauthorized/department/detail
     * @param departmentID 学院ID
     * @return 学院详情
     */
    @GetMapping(value = "/department/detail")
    public ResponseEntity<ResultEntity<Object>> getDepartmentDetail(
            @RequestParam(value = "departmentID") Integer departmentID
    ) {
        return ResultEntity.success(
                200,
                "Get the department's details successfully",
                departmentService.getDepartmentDetail(departmentID));
    }

    /**
     * 获取所有学院
     * GET /unauthorized/department
     * @return 学院列表
     */
    @GetMapping(value = "/department")
    public ResponseEntity<ResultEntity<Object>> getDepartment() {
        return ResultEntity.success(
                200,
                "Get all departments successfully",
                departmentService.getDepartment()
        );
    }

    /**
     * 获取学院录取学生
     * GET /unauthorized/enroll
     * @param department 学院
     * @param year 年份
     * @return 录取学生列表
     */
    @GetMapping(value = "/enroll")
    public ResponseEntity<ResultEntity<Object>> getEnrollTable(
            @RequestParam(value = "departmentID") Integer department,
            @RequestParam(value = "year") Integer year
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get enroll table successfully",
                enrollService.getEnrollTable(department, year)
        );
    }

    @GetMapping(value = "/notice")
    public ResponseEntity<ResultEntity<Object>> getNotice(
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get notice successfully",
                noticeService.getAllNotice(pageIndex, pageSize)
        );
    }

    @GetMapping(value = "/notice/detail")
    public ResponseEntity<ResultEntity<Object>> getNoticeDetail(
            @RequestParam(value = "noticeID") Integer noticeID
    ) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Get notice detail successfully",
                noticeService.getNoticeDetail(noticeID)
        );
    }

}
