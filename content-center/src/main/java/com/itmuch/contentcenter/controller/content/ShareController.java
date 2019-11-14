package com.itmuch.contentcenter.controller.content;

import com.github.pagehelper.PageInfo;
import com.itmuch.contentcenter.auth.CheckLogin;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.service.content.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/q")
    public PageInfo<Share> list(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String title) {

        if (pageSize > 100) {
            pageSize = 100;
        }
        PageInfo<Share> q = shareService.q(pageNo, pageSize, title);
        return q;
    }
}
