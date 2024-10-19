package com.jackson.handler;

import cn.hutool.json.JSONUtil;
import com.jackson.entity.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 自定义授权失败处理器
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 返回json格式的错误信息
        Result error = Result.error(accessDeniedException.getMessage());
        String jsonStr = JSONUtil.toJsonStr(error);
        response.getWriter().write(jsonStr);
    }
}
