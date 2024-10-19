package com.jackson.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/success")
    public String success() {
        return "success";
    }

    @RequestMapping("/fail")
    public String fail() {
        return "fail";
    }

    @RequestMapping("/userLogout")
    public String logout() {
        return "userLogout";
    }

    @GetMapping("/username")
    public String getUsername() {
        // 用户认证通过后，为了避免用户的每次操作都进行认证可将用户的信息保存在会话中。spring security提供会话管理，认证通过后将身份信息放入SecurityContextHolder上下文，SecurityContext与当前线程进行绑定，方便获取 用户身份。
        // 获取用户认证后存储的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取用户名 -> springSecurity自动存储的Principal是一个UserDetails对象
        Object principal = authentication.getPrincipal();
        String username = null;
        // 判断principal是不是UserDetails的实例
        if (principal instanceof UserDetails) {
            // 是 -> 获取UserDetails中的用户名
            username = ((UserDetails) principal).getUsername();
        } else {
            // 不是 -> principal就是用户名
            username = principal.toString();
        }
        return username;
    }
}
