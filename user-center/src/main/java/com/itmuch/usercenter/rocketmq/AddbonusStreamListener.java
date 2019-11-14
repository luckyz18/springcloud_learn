package com.itmuch.usercenter.rocketmq;


import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AddbonusStreamListener {
    @Resource
    private UserService userService;

    @StreamListener(Sink.INPUT)
    public void receive(UserAddBonusMsgDto message){
        message.setDescription("投稿加积分、、");
        message.setEvent("CONTRIBUTE");
        this.userService.addBonus(message);
    }


}
