package com.itmuch.usercenter.util;

import com.alibaba.nacos.client.identify.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * JwtOperator 测试用例
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTTest {
    @Autowired
    private JwtOperator jwtOperator;

    private String token = "";

    @Before
    public void generateTokenTest() {
        // 设置用户信息
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", "lihua");

        // 测试1: 生成token
        this.token = jwtOperator.generateToken(objectObjectHashMap);
        // 会生成类似该字符串的内容: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE1NjU1ODk4MTcsImV4cCI6MTU2Njc5OTQxN30.27_QgdtTg4SUgxidW6ALHFsZPgMtjCQ4ZYTRmZroKCQ
        System.out.println(this.token);
    }

    @Test
    public void validateTokenTest() {
        // 测试2: 如果能token合法且未过期，返回true
        Boolean validateToken = jwtOperator.validateToken(this.token);
        System.out.println("token校验结果：" + validateToken);
    }

    @Test
    public void getClaimsFromTokenTest() {
        // 测试3: 解密token，获取用户信息
        Claims claims = jwtOperator.getClaimsFromToken(this.token);
        System.out.println(claims);
    }

    @Test
    public void decodeHeaderTest() {
        // 获取Header，即token的第一段（以.为边界）
        String[] split = this.token.split("\\.");
        String encodedHeader = split[0];

        // 测试4: 解密Header
        byte[] header = Base64.decodeBase64(encodedHeader.getBytes());
        System.out.println(new String(header));
    }

    @Test
    public void decodePayloadTest() {
        // 获取Payload，即token的第二段（以.为边界）
        String[] split = this.token.split("\\.");
        String encodedPayload = split[1];

        // 测试5: 解密Payload
        byte[] payload = Base64.decodeBase64(encodedPayload.getBytes());
        System.out.println(new String(payload));
    }

    @Test(expected = SignatureException.class)
    public void validateErrorTokenTest() {
        try {
            // 测试6: 篡改原本的token，因此会报异常，说明JWT是安全的
            jwtOperator.validateToken(this.token + "xx");
        } catch (SignatureException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
