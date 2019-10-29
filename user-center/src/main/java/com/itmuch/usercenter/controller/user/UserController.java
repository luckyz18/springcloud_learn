package com.itmuch.usercenter.controller.user;

import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/q")
    public User query(User user){
        return user;
    }

}
