package com.itmuch.contentcenter;

import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.itmuch")  //配置通用mapper
@SpringBootApplication
@EnableFeignClients
//feign全局配置  UserCenterFeignClien 可以改名为 GlobalFeignClient
//@EnableFeignClients(defaultConfiguration = UserCenterFeignClient.class)
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

}
