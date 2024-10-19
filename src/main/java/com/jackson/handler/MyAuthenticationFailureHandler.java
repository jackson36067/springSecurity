package com.jackson.handler;

import cn.hutool.json.JSONUtil;
import com.jackson.entity.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 自定义登录失败处理器
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 返回错误信息
        Result error = Result.error(exception.getMessage());
        String jsonStr = JSONUtil.toJsonStr(error);
        response.getWriter().write(jsonStr);
    }
}
