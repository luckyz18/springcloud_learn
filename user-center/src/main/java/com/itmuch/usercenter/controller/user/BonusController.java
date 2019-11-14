package com.itmuch.usercenter.controller.user;

import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.domain.dto.user.UserAddBonusDto;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class BonusController {

    @Autowired
    private UserService userService;

    @PostMapping("/add-bonus")
    public User addBonus(@RequestBody UserAddBonusDto userAddBonusDto){
        Integer bonus = userAddBonusDto.getBonus();
        Integer uid = userAddBonusDto.getUid();
        this.userService.addBonus(
                UserAddBonusMsgDto.builder()
                        .uid(uid)
                        .bonus(bonus)
                        .description("积分兑换，-" + bonus)
                        .event("购买")
                        .build()
        );
        return this.userService.findById(uid);
    }
}
