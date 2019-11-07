package com.itmuch.usercenter.auth;

import com.itmuch.usercenter.security.MySecurityException;
import com.itmuch.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CheckLoginAspect {

    @Autowired
    private JwtOperator jwtOperator;

    //加了 checklogin 注解的方法都会走到这里
    @Around("@annotation(com.itmuch.usercenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws MySecurityException {
        try {
            //1. 获取request token
            //spring 静态的方法获取request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("X-Token");

            //2. 校验 token 是否合法
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid){
                throw new MySecurityException("token不合法！");
                //throw new RuntimeException("token 不合法");
            }

            //3. 校验成功 就将用户信息设置到request
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id",claims.get("id"));
            request.setAttribute("wxNickname",claims.get("wxNickname"));
            request.setAttribute("roles",claims.get("roles"));

            //放行 执行后面的业务逻辑
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new MySecurityException("token不合法");
        }

    }

}
