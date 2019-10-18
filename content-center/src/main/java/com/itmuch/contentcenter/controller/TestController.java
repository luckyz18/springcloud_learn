package com.itmuch.contentcenter.controller;

import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.feignclient.TestBaiduFeignClient;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))  //+final
public class TestController {
   /* @Resource
    private ShareMapper shareMapper;
*/
    //@GetMapping("/test2")
    /*public List<Share> testInsert() {
        Share share = new Share();
        share.setAuditStatus("1");
        share.setAuthor("aa");
        share.setBuyCount(1);
        share.setCover("kk");
        share.setTitle("title");
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setUserId(1);

        shareMapper.insertSelective(share);
        List<Share> shares = shareMapper.selectAll();
        return shares;


    }*/
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test2")
    public List<ServiceInstance> getInstance(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }

    @Autowired
    TestBaiduFeignClient baiduFeignClient;
    @GetMapping("/baidutest")
    public String testBaidu(){
        return baiduFeignClient.index();
    }






}
