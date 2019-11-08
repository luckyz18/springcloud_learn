package com.itmuch.contentcenter.configuration;

import com.itmuch.contentcenter.feignclient.intercepter.TokenRelayRequestIntercepter;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration  这个注解不能加 ，父子上下文的问题，重复扫描
//feign的配置类，不加@Configuration，否则移到 scan扫描的包以外；否则所有的FeignClient 都会变成这个config
// 注：该类不要加上@Configuration注解，否则将会因为父子上下文扫描重叠而成为全局配置
public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level(){
        // feign 打印所有日志
        return Logger.Level.FULL;
    }

    //这是配置config的方式  配置feign 的 RequestInterceptor
    // 还可以用配置文件的方式
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TokenRelayRequestIntercepter();
    }
}
