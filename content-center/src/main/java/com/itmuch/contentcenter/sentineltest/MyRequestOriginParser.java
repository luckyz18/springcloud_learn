package com.itmuch.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.nacos.client.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现区分来源，对流控和授权规则的来源进行限制
 * 一般放到 header把来源
 */
//@Component  //失效
@Slf4j
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 从header中获取名为 origin 的参数并返回
        String origin = httpServletRequest.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            // 如果获取不到，则抛异常
            String err = "origin param must not be blank!";
            log.error("parse origin failed: {}", err);
            throw new IllegalArgumentException(err);
        }
        return origin;
    }
}
