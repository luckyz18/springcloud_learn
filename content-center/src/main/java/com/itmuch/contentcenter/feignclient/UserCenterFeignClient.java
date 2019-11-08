package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.configuration.UserCenterFeignConfiguration;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

// name : 远程调用的 微服务名称
//局部配置-java 代码配置
//@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
//局部配置 - 配置文件配置

@FeignClient(name = "user-center"
        //fallback = UserCenterFeignClientFallBack.class,
        //可以捕获到异常，但是不能和上面那个一起使用
        //fallbackFactory = UserCenterFeignClientFallBackFactory.class
        //configuration = UserCenterFeignConfiguration.class
)
public interface UserCenterFeignClient {
    /**
     * 当方法被调用的时候 feign 就会构造出这样的URL：  http://user-center/user/{id}，并且去请求，响应的部分封装成 UserDto
     * restTemplate 就可以功成身退了
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    //UserDto findById(@PathVariable Integer id);
    //和 controller 那里的@RequestHeader 定义不太一样 controler是把token拿进来，feign是送出去
    UserDto findById(@PathVariable Integer id
                     //@RequestHeader(value = "X-Token") String token
    );

    @GetMapping("/users/q")
    public UserDto findTest(@SpringQueryMap UserDto userDto);



}
