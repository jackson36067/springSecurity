package com.jackson.config;


import com.jackson.filter.JwtAuthenticationTokenFilter;
import com.jackson.handler.MyAccessDeniedHandler;
import com.jackson.handler.MyAuthenticationFailureHandler;
import com.jackson.handler.MyAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
//@EnableWebSecurity  // 开启springSecurity的自动配置 (springboot项目中可以省略)

// 开启springSecurity注解方式的授权 + 鉴权
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    /**
     * 配置密码加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用Bcrypt密码加密器
        return new BCryptPasswordEncoder();
    }


    /**
     * 配置自定义的登录认证方式
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public DefaultSecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        // 去除springSecurity默认的登录认证, 使用自定义登录认证 --- 需要定义所有拦截以及认证操作
        httpSecurity
                // 禁用csrf安全保护
                .csrf(AbstractHttpConfigurer::disable)
                // 授权配置
                .authorizeHttpRequests(authorization -> {
                    authorization
                            .requestMatchers("/depts/insert", "depts/update").hasAuthority("dept")
                            .requestMatchers("/depts/delete", "/depts/list").hasAuthority("hr")
                            .requestMatchers("/jwt/login")
                            // 允许所有匹配成功的路径放行
                            .permitAll()
                            // 匹配所有路径
                            .anyRequest()
                            // 认证拦截
                            .authenticated();
                })
                // 登录配置
                .formLogin(
                        formLogin -> formLogin
                                // 登录成功后执行的处理器
                                .successHandler(new MyAuthenticationSuccessHandler())
                                // 登录失败后执行的处理器
                                .failureHandler(new MyAuthenticationFailureHandler())
                )
                // 会话管理配置 -> 放弃session模式, 改用自定义的jwt
                .sessionManagement(sessionManager -> {
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 设置成无状态登录
                })
                // 异常处理
                .exceptionHandling(exceptionHandling -> {
                    // 授权异常处理
                    exceptionHandling.accessDeniedHandler(new MyAccessDeniedHandler());
                    // 登录时异常处理
                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write("has error：" + authException.getMessage());
                    });
                })

                // 过滤器控制
                // 在springSecurity用户认证UsernamePasswordAuthenticationFilter过滤器前添加自定义的过滤器, 保证认证流程先执行自定义的jwt认证
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return httpSecurity.build();
    }


    // 没有使用jwt之前, 使用spring security 默认的session登录 这个对象框架自动默认操作
    // 使用jwt, 放弃使用spring security 默认的session登录, 意味着需要自己去控制登录逻辑
    // 此时需要在spring security 认证框架规则基础上执行自定义jwt登录认证
    // 所谓认证框架爱规则基础: 还是遵循spring security认证流程
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // 配置用户详情服务等
        return authenticationManagerBuilder.build();
    }

}