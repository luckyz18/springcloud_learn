package com.itmuch.contentcenter.controller.content;

import com.itmuch.contentcenter.auth.CheckLogin;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.service.content.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shares")
public class ShareController {
    @Autowired
    private ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDto findById(@PathVariable Integer id
                             //@RequestHeader(value = "X-Token") String token
    ) {
        return shareService.findById(id);
    }

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @GetMapping("/testget")
    public UserDto testGet(UserDto userDto) {
        UserDto test = userCenterFeignClient.findTest(userDto);
        return test;
    }
}
