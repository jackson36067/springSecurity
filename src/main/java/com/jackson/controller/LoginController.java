package com.jackson.controller;

import com.jackson.utils.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Resource
    private AuthenticationManager authenticationManager;

    @PostMapping("/jwt/login")
    public String login(String uname, String pwd) {
        // 根据前端传递的用户名密码生成UsernamePasswordAuthenticationToken,用于进一步校验逻辑
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uname, pwd);
        // 在springSecurity规则下, 自定义校验逻辑
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate != null && authenticate.isAuthenticated()) {
            //登录成功
            return JWTUtils.createToken("username", uname);
        }
        throw new RuntimeException("账号不存在或密码出错");
    }
}