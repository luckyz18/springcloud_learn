package com.itmuch.contentcenter;

import com.itmuch.contentcenter.restTemplateInterceptor.RestTemplateInterceptor;
import com.itmuch.contentcenter.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collections;


//@MapperScan("com.itmuch")  //配置通用mapper
//配置只扫描dao的接口 不然有一个 mysource接口，会报错，他并不是mybatis的配置类
@MapperScan("com.itmuch.contentcenter.dao")
@EnableFeignClients
//feign全局配置  UserCenterFeignClien 可以改名为 GlobalFeignClient
//@EnableFeignClients(defaultConfiguration = UserCenterFeignClient.class)
@SpringBootApplication
//spring-cloud-stream 注解
@EnableBinding({Source.class, MySource.class})
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    // restTemplate整合 sentinel实现对服务提供者的接口容错
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        //return  new RestTemplate();
        //添加一个 RestTemplate 拦截器 传递token
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(
                Collections.singletonList(
                        new RestTemplateInterceptor()
                )
        );
        return restTemplate;
    }
}
