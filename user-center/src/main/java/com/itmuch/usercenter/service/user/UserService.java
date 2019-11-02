package com.itmuch.usercenter.service.user;

import com.itmuch.usercenter.dao.user.BonusEventLogMapper;
import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    //为用户添加积分
    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDto msgDto){
        // 为用户加积分
        User user = userMapper.selectByPrimaryKey(msgDto.getUid());
        Integer bonus = msgDto.getBonus();
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
        log.info("积分添加完毕...");
    }

}
