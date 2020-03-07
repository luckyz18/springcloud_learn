package com.itmuch.usercenter.rocketmq;

import com.itmuch.usercenter.dao.user.BonusEventLogMapper;
import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

//@Service
//topic 属性一定要和生产者的主题名称一致
//consumerGroup  消费者的Group 是放在属性上的
//用stream 实现收消息 先把这个注释掉
//@RocketMQMessageListener(topic = "add-bonus",consumerGroup = "consumer-group")
//@Slf4j
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDto> {

    @Resource
    private  UserMapper userMapper;
    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMsgDto message) {
        /*//收到消息 执行的业务
        // 为用户加积分
        User user = userMapper.selectByPrimaryKey(message.getUid());
        Integer bonus = message.getBonus();
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKeySelective(user);

        // 在日志表里添加一条消息
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(user.getId())
                .value(bonus)
                .createTime(new Date())
                .description("添加积分" + bonus)
                .event("发布了一个资源")
                .build()
        );
        log.info("积分添加完毕...");*/
    }
}
