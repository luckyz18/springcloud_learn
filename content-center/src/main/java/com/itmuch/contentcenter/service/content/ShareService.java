package com.itmuch.contentcenter.service.content;

import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.content.ShareAuditDto;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.sound.sampled.Line;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShareService {
    @Resource
    private ShareMapper shareMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    public ShareDto findById(Integer id) {
        Share share = shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();
/*
       //下面简化这段代码  用 Ribbon 实现负载均衡
       List<ServiceInstance> instans = discoveryClient.getInstances("user-center");
        List<String> targetURLs = instans.stream()
                //数据变换
                .map(instance -> instance.getUri().toString() + "/users/{id}")
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("当前没有该服务"));
                .collect(Collectors.toList());
        int i = ThreadLocalRandom.current().nextInt(targetURLs.size());
        String targetURL = targetURLs.get(i);
        log.info(targetURL);
        UserDto userDto = this.restTemplate.getForObject(targetURL, UserDto.class, userId);
*/
/*

        //下面改造，不用restTemplate， 用Feign请求
        缺点：   1. 代码不可读
                2. 复杂的URL 难以维护
                3. 难以响应需求的变化，变化很没有幸福感
                4. 编程体验不统一
        UserDto userDto = this.restTemplate.getForObject("http://user-center/users/{userId}", UserDto.class, userId);
*/
        UserDto userDto  = this.userCenterFeignClient.findById(userId);


        ShareDto shareDto = new ShareDto();
        //消息装配
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }

    /*管理员审核*/
    public Share auditById(Integer id, ShareAuditDto auditDto) {
        //1. 查看该分享是否存在 && 状态是否是待审核状态
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null){
            new IllegalArgumentException("参数非法，分享内容不存在");
        }
        if (Objects.equals("NOT_YET",share.getAuditStatus())){
            new IllegalArgumentException("参数非法，不是待审核状态");
        }

        //2. 审核资源 更新审核状态
        share.setAuditStatus(auditDto.getAuditStatusEnum().toString());
        shareMapper.updateByPrimaryKey(share);

        //如果是pass 为发布人添加积分
        //假设这个方法飞创耗时  那么优化一下  变成异步请求，提升用户体验
        //userCenterFeignClient.addBonus(uid,500);
        return share;
    }
}
