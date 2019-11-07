package com.itmuch.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.itmuch.usercenter.domain.dto.user.JwtTokenRespDTO;
import com.itmuch.usercenter.domain.dto.user.LoginRespDTO;
import com.itmuch.usercenter.domain.dto.user.UserLoginDTO;
import com.itmuch.usercenter.domain.dto.user.UserRespDTO;
import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.service.user.UserService;
import com.itmuch.usercenter.util.JwtOperator;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private JwtOperator jwtOperator;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/q")
    public User query(User user) {
        return user;
    }

    /**
     * 微信登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {

        //微信小程序服务端校验是否已经登录的结果  没登录直接抛异常
        WxMaJscode2SessionResult result = wxMaService.getUserService().
                getSessionInfo(loginDTO.getCode());


        //微信的openID 用户在微信这边的唯一标识  openid就是 微信id
        String openid = result.getOpenid();

        //查看用户是否注册  没注册就插入
        //注册过就 直接办法token

        User user = this.userService.login(loginDTO, openid);

        //颁发token
        Map userInfo = new HashMap();
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("roles", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        log.info("用户{} 生成的额token ={}", user.getWxNickname(), token);

        //构造返回信息
        return LoginRespDTO.builder()
                .user(
                        UserRespDTO.builder()
                                .id(user.getId())
                                .avatarUrl(user.getAvatarUrl())
                                .bonus(user.getBonus())
                                .wxNickname(user.getWxNickname())
                                .build()
                )
                .token(JwtTokenRespDTO.builder()
                        .token(token)
                        .expirationTime(jwtOperator.getExpirationDateFromToken(token).getTime())
                        .build())
                .build();
    }
}
