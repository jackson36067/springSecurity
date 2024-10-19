package com.jackson.config;


import com.jackson.handler.MyAccessDeniedHandler;
import com.jackson.handler.MyAuthenticationFailureHandler;
import com.jackson.handler.MyAuthenticationSuccessHandler;
import com.jackson.handler.MyLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;


//@Configuration
//@EnableWebSecurity  // 开启springSecurity的自动配置 (springboot项目中可以省略)

// 开启springSecurity注解方式的授权 + 鉴权
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig_bak {
    /**
     * 基于内存创建用户信息
     * UserDetailsService --- springSecurity 提供专门用于加载用户信息服务层接口
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 创建基于内存的用户信息管理器
        InMemoryUserDetailsManager memory = new InMemoryUserDetailsManager();
        // 使用manager管理UserDetails对象
        memory.createUser(
                // 创建UserDetails对象, 用户管理用户名, 用户密码, 用户角色, 用户权限
                User.withUsername("jackson")
                        .password("123456")
                        //.authorities("dept:update", "dept:insert")
                        .roles("DEPT_MGR")
                        .build()
        );
        return memory;
    }


    /**
     * 配置密码加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 不使用任何密码加密器
        return NoOpPasswordEncoder.getInstance();
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
                            // 角色配置授权方式 --- 想要访问接口就要用户这个角色
                            /*.requestMatchers("/depts/insert", "depts/update").hasRole("dept_mgr")
                            .requestMatchers("/depts/delete", "/depts/list").hasRole("hr")*/
                            //  权限配置授权方式 --- 要访问这个接口就必须拥有这个权限
                            /*.requestMatchers("/depts/insert").hasAuthority("dept:insert")
                            .requestMatchers("/depts/update").hasAuthority("dept:update")
                            .requestMatchers("/depts/delete").hasAuthority("dept:delete")
                            .requestMatchers("/depts/list").hasAuthority("dept:list")*/
                            // 匹配相应的路径
                            .requestMatchers("/login.html", "userLogin", "fail", "/userLogout")
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
                                .loginProcessingUrl("/userLogin") // 设置登录请求路径url 默认值为login
                                .loginPage("/login.html") // 设置登录页
                                .usernameParameter("uname") // 设置登录时提交用户名参数
                                .passwordParameter("pwd") // 设置登录时提交密码参数
                                //.successForwardUrl("/success") // 登录成功后跳转的路径
                                .successHandler(new MyAuthenticationSuccessHandler()) // 登录成功后执行的处理器
                                //.failureForwardUrl("/fail") // 登录失败后跳转的路径
                                .failureHandler(new MyAuthenticationFailureHandler()) // 登录失败后执行的处理器
                )
                // 登出配置
                .logout(
                        logout -> {
                            logout
                                    .logoutUrl("/userLogout") // 设置登出请求路径 默认值为logout
                                    //.logoutSuccessUrl("/userLogout") // 设置登出成功跳转的路径
                                    .logoutSuccessHandler(new MyLogoutSuccessHandler());
                        }
                )
                // 异常处理配置
                .exceptionHandling(exceptionHandler -> {
                    exceptionHandler
                            // 当授权失败时跳转的页面
                            //.accessDeniedPage("/nopermission.html")
                            // 当授权失败时执行的处理器
                            .accessDeniedHandler(new MyAccessDeniedHandler());
                })
                // 会话管理配置 -> 放弃session模式, 改用自定义的jwt
                .sessionManagement(sessionManager -> {
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
        ;
        return httpSecurity.build();
    }
}