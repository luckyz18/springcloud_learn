package com.itmuch.contentcenter.service.content;

import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.domain.entity.content.Share;
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

        UserDto userDto = this.restTemplate.getForObject("http://user-center/users/{userId}", UserDto.class, userId);

        ShareDto shareDto = new ShareDto();
        //消息装配
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }


}
