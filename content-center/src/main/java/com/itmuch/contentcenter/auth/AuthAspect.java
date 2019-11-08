package com.itmuch.contentcenter.auth;

import com.itmuch.contentcenter.Util.JwtOperator;
import com.itmuch.contentcenter.exception.SecuException;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtOperator jwtOperator;

    //加了 checklogin 注解的方法都会走到这里
    @Around("@annotation(com.itmuch.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        checkToken();
        //放行 执行后面的业务逻辑
        return joinPoint.proceed();
    }

    //检验 token 是否合法
    private void checkToken() {
        try {
            //1. 获取request token
            //spring 静态的方法获取request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("X-Token");

            //2. 校验 token 是否合法
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                //throw new SecuException("token 错误 用户不允许访问 isValid！");
                throw new SecuException("token 错误 用户不允许访问 isValid");
            }

            //3. 校验成功 就将用户信息设置到request
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claims.get("id"));
            request.setAttribute("wxNickname", claims.get("wxNickname"));
            request.setAttribute("roles", claims.get("roles"));
        } catch (Throwable throwable ) {
            throw new SecuException("token 错误 用户不允许访问 SecuException");
        }
    }

    /**
     * 检验身份认证
     *
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.itmuch.contentcenter.auth.CheckAuthorization)")
    public Object checkAuthority(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            //1. 校验 token
            this.checkToken();
            //2. 校验 角色是否匹配
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String role = (String) request.getAttribute("roles");
            //获取注解上的角色
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();
            if (!Objects.equals(role, value)) {
                throw new SecuException("角色1-用户，用户无权访问！");
            }
        } catch (Throwable throwable) {
            throw new SecuException("角色2-用户，用户无权访问！");
        }

        //如果这里发生异常 不会报无权访问的异常了
        return joinPoint.proceed();
    }

}
