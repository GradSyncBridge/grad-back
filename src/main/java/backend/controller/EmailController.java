package backend.controller;

import backend.annotation.SysLog;
import backend.service.EmailService;
import backend.util.ResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/unauthorized/email")
    public ResponseEntity<ResultEntity<Object>> sendResetPasswordEmail(@Param(value = "email") String email) {
        emailService.sendResetPasswordEmail(email);
        return ResultEntity.success(HttpStatus.OK.value(), "Send email successfully");
    }

    @GetMapping(value = "/unauthorized/email/{token}")
    public ResponseEntity<ResultEntity<Object>> sendVerificationEmail(@PathVariable(value = "token") String token) {
        emailService.redirectToLoginPage(token);
        return ResultEntity.success(HttpStatus.OK.value(), "Send email successfully");
    }
}
