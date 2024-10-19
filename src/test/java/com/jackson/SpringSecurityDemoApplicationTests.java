package com.jackson;

import cn.hutool.core.lang.Assert;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

//@SpringBootTest
class SpringSecurityDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testPassword() {

        // 工作因子，默认值是10，最小值是4，最大值是31，值越大运算速度越慢
        PasswordEncoder encoder = new BCryptPasswordEncoder(4);
        //明文："password"
        //密文：result，即使明文密码相同，每次生成的密文也不一致
        String result = encoder.encode("123456");
        System.out.println(result);
        //密码校验
        Assert.isTrue(encoder.matches("123456", result), "密码不一致");
    }

    // 创建jwt令牌
    @Test
    void testCreateJwt() {
        String jwt = JWT
                .create()
                .withClaim("username", "jackson")
                .sign(Algorithm.HMAC256("jackson".getBytes(StandardCharsets.UTF_8)));
        System.out.println(jwt);
    }

    // 解析令牌
    @Test
    void parseJwt() {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jackson".getBytes(StandardCharsets.UTF_8))).build();
        String username = verifier
                .verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjkxNTc4NTEsInVzZXJuYW1lIjoiamFja3NvbiJ9.s4VSMzbymxlDW7yFkjfmivhwJo-Q2raWrkp0WX4jDu0")
                .getClaim("username")
                .asString();
        System.out.println(username);
    }

    // 测试给jwt添加有效时间
    @Test
    void testJwtExpireTime() throws InterruptedException {
        String jwt = JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 5000L))
                .withClaim("username", "jackson")
                .sign(Algorithm.HMAC256("jackson".getBytes(StandardCharsets.UTF_8)));
        System.out.println(jwt);
        // Thread.sleep(6000L);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("jackson".getBytes(StandardCharsets.UTF_8))).build();
        String username = verifier.verify(jwt).getClaim("username").asString();
        System.out.println(username);
    }
}
