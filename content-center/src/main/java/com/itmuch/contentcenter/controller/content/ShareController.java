package com.itmuch.contentcenter.controller.content;

import com.github.pagehelper.PageInfo;
import com.itmuch.contentcenter.Util.JwtOperator;
import com.itmuch.contentcenter.auth.CheckLogin;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.content.ShareRequestDTO;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.domain.entity.content.MidUserShare;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.service.content.ShareService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/shares")
public class ShareController {
    @Autowired
    private ShareService shareService;
    @Autowired
    JwtOperator jwtOperator;

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
                                @RequestParam(required = false) String title,
                                @RequestHeader(value = "X-Token", required = false) String token) {

        if (pageSize > 100) {
            pageSize = 100;
        }

        //如果用户未登陆 那么downloadURL 设置成空
        //如果用户登录  查看有没有购买  没有购买是空  购买是下载URL
        Integer userId = null;
        if (StringUtils.isNotBlank(token)) {
            Claims claims = jwtOperator.getClaimsFromToken(token);
            userId = (Integer) claims.get("id");
        }

        PageInfo<Share> q = shareService.q(pageNo, pageSize, title, userId);
        return q;
    }

    /**
     * 积分兑换指定分享
     */
    @GetMapping("/exchange/{id}")
    @CheckLogin
    public Share exchangeById(@PathVariable Integer id) {
        return this.shareService.exchangeById(id);
    }

    /**
     * 投稿
     */
    @PostMapping("/contribute")
    @CheckLogin
    public Share contribute(@RequestBody ShareRequestDTO shareRequestDTO) {
        //spring 静态的方法获取request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("X-Token");
        Claims claims = jwtOperator.getClaimsFromToken(token);
        Integer uid = Integer.valueOf(claims.get("id").toString());
        return this.shareService.addContribute(shareRequestDTO, uid);
    }

    @GetMapping("/preview/{id}")
    public ShareDto previewById(@PathVariable Integer id) {
        return this.shareService.findById(id);
    }

    @GetMapping("/my/contributions")
    @CheckLogin
    public List<Share> getMyContributes() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("X-Token");
        Claims claims = jwtOperator.getClaimsFromToken(token);
        Integer uid = Integer.valueOf(claims.get("id").toString());
        return this.shareService.getContributesByUid(uid);
    }

    /**
     * 编辑投稿
     */
    @PutMapping("/contribute/{id}")
    public ShareDto editContributeById(@PathVariable Integer id,
                                       @RequestBody ShareRequestDTO shareRequestDTO) {
        return this.shareService.updateContributeById(id, shareRequestDTO);
    }

    /**
     * 查询当前登录用户拥有的share列表
     */
    @GetMapping("/user")
    @CheckLogin
    public List<Share> getMyShares(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("X-Token");
        Claims claims = jwtOperator.getClaimsFromToken(token);
        Integer userId = Integer.valueOf(claims.get("id").toString());
        return this.shareService.getMyShares(userId);
    }
}
