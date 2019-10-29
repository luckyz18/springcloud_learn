package com.itmuch.usercenter.rocketmq;

import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//topic 属性一定要和生产者的主题名称一致
//consumerGroup  消费者的Group 是放在属性上的
@RocketMQMessageListener(topic = "add-bonus",consumerGroup = "consumer-group")
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDto> {
    //@Autowired
    //private UserMapper userMapper;



    @Override
    public void onMessage(UserAddBonusMsgDto userAddBonusMsgDto) {
        //收到消息 执行的业务
        // 为用户加积分

        // 在日志表里添加一条消息



    }
}
