package com.jackson.filter;

import com.jackson.utils.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.setContentType("application/json;charset=utf-8");

        // 登录接口不拦截
        String url = httpServletRequest.getRequestURI(); // 使用URI方法默认会去掉 http:localhost:8080// 前缀
        if (url.startsWith("/jwt/login") || url.startsWith("/doc.html")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 判断token是否为空
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.hasText(token)) {
            httpServletResponse.getWriter().write("token 不能为空");
            return;
        }
        // 判断token的合法性
        if (!JWTUtils.isExpired(token)) {
            httpServletResponse.getWriter().write("token 失效");
            return;
        }

        // 获取登录时在token中的储存的用户名
        String username = JWTUtils.getToken(token, "username");

        // 判断SecurityContextHolder中的context中是否存储Authentication用户认证对象 -> 为空说明此时需要往SecurityContextHolder中的context中存储Authentication
        // 作用: 以便springSecurity中的登录认证过滤器会直接放行, 也可以减少对数据库的访问
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // 通过userDetails类中的loadUserByUsername方法根据获取的用户名从数据库中查询该用户, 用于获取用户的用户名,密码以及权限
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                httpServletResponse.getWriter().write("token校验失败，请重新登录");
                return;
            }
            //  由于设置了无状态登录,在SecurityContextHolder类中就不会缓存Authentication用户信息认证对象
            //  由于后续过滤器练需要使用到Authentication用户信息认证对象(比如授权),这里需要将jwt转换成Authentication用户信息认证对象缓存到SecurityContextHolder中
            //  由于SecurityContextHolder中缓存了用户信息认证对象,所以springSecurity中的登录认证过滤器会直接放行
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}