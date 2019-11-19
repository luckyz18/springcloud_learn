package com.itmuch.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.itmuch.usercenter.auth.CheckLogin;
import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.domain.dto.user.*;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.service.user.BonusEventLogService;
import com.itmuch.usercenter.service.user.UserService;
import com.itmuch.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    BonusEventLogService bonusEventLogService;

    @GetMapping("/{id}")
    //自定义的一个注解
    @CheckLogin
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
        log.info("用户{} 生成的token ={}", user.getWxNickname(), token);

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
                .token(
                        JwtTokenRespDTO.builder()
                                .token(token)
                                .expirationTime(jwtOperator.getExpirationDateFromToken(token).getTime())
                                .build()
                )
                .build();
    }

    @GetMapping("/gen-token")
    public String getToken(){
        Map userInfo = new HashMap(3);
        userInfo.put("id", 4);
        userInfo.put("wxNickname", "鸭鸭");
        userInfo.put("roles", "admin");
        return  jwtOperator.generateToken(userInfo);
    }

    /**
     * 查询当前登录用户积分明细
     * @return
     */
    @GetMapping("/bonus-logs")
    public List<BonusEventLogDto> findAllBonusEventLog() {
        return this.bonusEventLogService.findAllBonusEventLog();
    }

    /**
     * 用户个人信息
     */
    @GetMapping("/me")
    @CheckLogin
    public User getUserInfo(){
        //静态获取token  获取uid
        String token = getTokenFromRequest();
        Integer userId = getUserIdFromToken(token);
        //根据uid 获取个人信息
        return this.userService.findById(userId);
    }

    private Integer getUserIdFromToken(String token) {
        Claims claims = jwtOperator.getClaimsFromToken(token);
        return Integer.valueOf(claims.get("id").toString());
    }

    private String getTokenFromRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getHeader("X-Token");
    }

    /**
     * 签到
     * bug: 签到完了之后今天就不能签到了  积分只加一次
     * @return
     */
    @GetMapping("/sign")
    @CheckLogin
    public User sign(){
        // 获取uid 将其积分值加上20
        String token = getTokenFromRequest();
        Integer userId = getUserIdFromToken(token);
        //先查今天是否已经签到  如果签到过了  就不要加积分
        BonusEventLog log = this.userService.getUserIfSign(userId);
        if (log != null){
            //签到过了
            User user = this.userService.findById(userId);
            return user;
        }
        UserAddBonusMsgDto msgDto = UserAddBonusMsgDto.builder()
                .uid(userId)
                .bonus(20)
                .description("签到得20积分")
                .event("签到")
                .build();
        userService.addBonus(msgDto);
        return userService.findById(userId);
    }







}
