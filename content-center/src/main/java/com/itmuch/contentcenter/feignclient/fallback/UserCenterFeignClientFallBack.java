//package com.itmuch.contentcenter.feignclient.fallback;
//
//import com.itmuch.contentcenter.domain.dto.user.UserDto;
//import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class UserCenterFeignClientFallBack implements UserCenterFeignClient {
//    @Override
//    public UserDto findById(Integer id) {
//        // 自定义限流、降级发生时的处理逻辑
//        log.warn("远程调用被限流/降级了");
//        return UserDto.builder().
//                wxNickname("Default").
//                build();
//    }
//
//    @Override
//    public UserDto findTest(UserDto userDto) {
//        return null;
//    }
//}
