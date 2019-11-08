package com.itmuch.contentcenter.feignclient.intercepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现对feign 请求之前进行操作
 */
public class TokenRelayRequestIntercepter implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //静态 request 获取请求体  这个request 是/shres/1的
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("X-Token");
        //如果没带token的请求 就不用加上 token了
        if (token != null) {
            requestTemplate.header("X-Token",token);
        }
    }
}
