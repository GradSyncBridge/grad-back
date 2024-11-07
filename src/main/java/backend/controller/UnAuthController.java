package backend.controller;

import backend.model.DTO.UserLoginDTO;
import backend.model.DTO.UserRegisterDTO;
import backend.model.VO.user.UserLoginVO;
import backend.model.VO.user.UserRegisterVO;
import backend.service.DepartmentService;
import backend.service.MajorService;
import backend.service.TeacherService;
import backend.service.UserService;
import backend.util.ResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * UnAuthController
 *
 * @function 处理与身份验证无关的请求
 * @function login 处理登录请求
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

    /**
     * 处理登录请求
     *
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping(value = "/user/login")
    public ResponseEntity<ResultEntity<Object>> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        UserLoginVO data = userService.login(userLoginDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Login successfully.", data);
    }

    /**
     * 处理注册请求
     *
     * @param userRegisterDTO 注册信息
     * @return token
     */
    @PostMapping(value = "/user/register")
    public ResponseEntity<ResultEntity<Object>> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        UserRegisterVO data = userService.register(userRegisterDTO);
        return ResultEntity.success(HttpStatus.OK.value(), "Register successfully.", data);
    }

    /**
     * 获取所有专业
     * @param department 学院
     * @return 专业列表
     */
    @GetMapping(value = "/catalogue")
    public ResponseEntity<ResultEntity<Object>> getCatalogue(@Param(value = "department") Integer department) {
        return ResultEntity.success(200, "Get all majors successfully", majorService.getCatalogue(department));
    }

    /**
     * 获取学院下属所有老师
     */
    @GetMapping(value = "/department/teachers")
    public ResponseEntity<ResultEntity<Object>> getTeacher(@RequestParam(value = "departmentID") Integer department) {
        return ResultEntity.success(200, "Get all teachers successfully", teacherService.getTeacher(department));
    }

    /**
     * 获取所有学院
     * @return 学院列表
     */
    @GetMapping(value = "/department")
    public ResponseEntity<ResultEntity<Object>> getDepartment() {
        return ResultEntity.success(200, "Get all departments successfully", departmentService.getDepartment());
    }

    /**
     * 获取对应二级学科教师列表
     * @param majorID 二级学科
     * @return 教师列表
     */
    @GetMapping(value = "/catalogue/teachers")
    public ResponseEntity<ResultEntity<Object>> getTeachersByCatalogue(@RequestParam(value = "majorID") Integer majorID) {
        return ResultEntity.success(200, "Get all departments successfully", teacherService.getTeachersByCatalogue(majorID));
    }
}
