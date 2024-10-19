package com.jackson.handler;

import cn.hutool.json.JSONUtil;
import com.jackson.entity.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 向前端返回一个JSON格式的数据
        Result success = Result.success("登出成功");
        String jsonStr = JSONUtil.toJsonStr(success);
        log.info("{}", jsonStr);
        response.getWriter().write(jsonStr);
    }
}
