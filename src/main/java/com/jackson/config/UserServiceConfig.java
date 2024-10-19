package com.jackson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Configuration
//@EnableWebSecurity
public class UserServiceConfig {
    /**
     * 基于内存创建用户信息
     * UserDetailsService --- springSecurity 提供专门用于加载用户信息服务层接口
     *
     * @return
     */
    // @Bean
    public UserDetailsService userDetailsService() {
        // 创建基于内存的用户信息管理器
        InMemoryUserDetailsManager memory = new InMemoryUserDetailsManager();
        // 使用manager管理UserDetails对象
        memory.createUser(
                // 创建UserDetails对象, 用户管理用户名, 用户密码, 用户角色, 用户权限
                User.withUsername("jackson")
                        .password("123456")
                        //.authorities("dept:update", "dept:insert")
                        .roles("dept_mgr")
                        .build()
        );
        return memory;
    }
}
