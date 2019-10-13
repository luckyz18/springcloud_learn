package com.itmuch.usercenter.service.user;

import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.entity.user.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User findById(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

}
