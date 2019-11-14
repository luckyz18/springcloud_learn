package com.itmuch.contentcenter.service.content;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itmuch.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.content.ShareAuditDto;
import com.itmuch.contentcenter.domain.dto.content.ShareDto;
import com.itmuch.contentcenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.domain.enums.AuditStatusEnum;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ShareService {
    @Resource
    private ShareMapper shareMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Resource
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Autowired
    Source source;


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
        UserDto userDto = this.userCenterFeignClient.findById(userId);


        ShareDto shareDto = new ShareDto();
        //消息装配
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(userDto.getWxNickname());
        return shareDto;
    }

    /*管理员审核*/
    //用 rmq 的 分布式事务去实现  -- 是用spring 的消息编程模型实现分布式事务
    public Share auditById(Integer id, ShareAuditDto auditDto) {
        //1. 查看该分享是否存在 && 状态是否是待审核状态
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            new IllegalArgumentException("参数非法，分享内容不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            new IllegalArgumentException("参数非法，不是待审核状态");
        }

/*      //2. 审核资源 更新审核状态
        share.setAuditStatus(auditDto.getAuditStatusEnum().toString());
        shareMapper.updateByPrimaryKey(share);

        //3. 如果是pass 为发布人添加积分
        //假设这个方法飞创耗时  那么优化一下  变成异步请求，提升用户体验
        //userCenterFeignClient.addBonus(uid,500);

        rocketMQTemplate.convertAndSend(
                "add-bonus",  //主题的名字
                UserAddBonusMsgDto.builder()
                        .uid(share.getUserId())
                        .bonus(50)
                        .build()
        );
*/
        // 如果是pass现在用rocketmq 处理事务消息
        // 1. 发送半消息
        if (AuditStatusEnum.PASS.equals(auditDto.getAuditStatusEnum())) {
            String transactionID = UUID.randomUUID().toString();
            /**
             * 1.1 这里是spring 的编程模型+ rmq实现分布式事物的
             */
            /*this.rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-bonus-group",
                    "add-bonus",
                    MessageBuilder.withPayload(
                            UserAddBonusMsgDto.builder()
                                    .uid(share.getUserId())
                                    .bonus(50)
                                    .build()
                    )
                            //header 有大用处  -- 可以传参
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionID)
                            .setHeader("share_id", id)
                            .build(),

                    // 这里的 arg 有大用处  在本地事务处理 executeLocalTransaction  那里，-- 可以传参
                    auditDto
            );*/

            /**
             * 1.2 这里是 spring-cloud-stream + rmq 实现分布式事务
             */
            this.source.output().send(
                    MessageBuilder.withPayload(
                            UserAddBonusMsgDto.builder()
                                    .uid(share.getUserId())
                                    .bonus(60)
                                    .build()
                    )
                            //header 有大用处  -- 可以传参
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionID)
                            .setHeader("share_id", id)
                            // 用header 来传 auditDTO  后面有个小坑--参数是String 类型的 而不是对象类型
                            //.setHeader("dto",auditDto)
                            .setHeader("dto", JSON.toJSONString(auditDto))
                            .build()
            );

        } else {
            //不发消息  只更新数据库状态
            auditByIdInDB(id, auditDto);
        }
        return share;
    }

    //审核资源
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(int shareId, ShareAuditDto auditDto) {
        //Share share = shareMapper.selectByPrimaryKey(shareId);
        //share.setAuditStatus(auditDto.getAuditStatusEnum().toString());
        Share share = Share.builder()
                .id(shareId)
                .auditStatus(auditDto.getAuditStatusEnum().toString())
                .reson(auditDto.getReson())
                .build();
        //注意这里只能 updateByPrimaryKeySelective  而不是updateByprimarykey
        this.shareMapper.updateByPrimaryKeySelective(share);

        // 把share写到缓存

    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithTransactionLog(int shareId, ShareAuditDto auditDto, String transactionID) {
        auditByIdInDB(shareId, auditDto);
        rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionID)
                        .log("审核分享...")
                        .build()
        );

    }

    public PageInfo<Share> q(Integer pageNo, Integer pageSize, String title) {
        PageHelper.startPage(pageNo,pageSize); // 这条语句切入一个不含分页的查询 xml    我觉得这样写真的很麻烦。。不如直接sql ne
        List<Share> shares = shareMapper.selecByParam(title);
        return new PageInfo<>(shares);
    }
}
