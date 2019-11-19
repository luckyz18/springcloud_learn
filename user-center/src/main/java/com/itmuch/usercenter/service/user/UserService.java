package com.itmuch.usercenter.service.user;

import com.itmuch.usercenter.auth.CheckLogin;
import com.itmuch.usercenter.dao.user.BonusEventLogMapper;
import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messages.UserAddBonusMsgDto;
import com.itmuch.usercenter.domain.dto.share.ShareDto;
import com.itmuch.usercenter.domain.dto.user.UserLoginDTO;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    //为用户添加积分
    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDto msgDto) {
        // 为用户加积分
        User user = userMapper.selectByPrimaryKey(msgDto.getUid());
        Integer bonus = msgDto.getBonus();
        Integer userId = user.getId();
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKeySelective(user);

        // 在日志表里添加一条消息
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .createTime(new Date())
                .description(msgDto.getDescription())
                .event(msgDto.getEvent())
                .build()
        );
        log.info("积分添加完毕...");
    }

    /**
     * 用户登录
     * @param loginDTO
     * @param openid
     * @return
     */
    public User login(UserLoginDTO loginDTO, String openid) {
        User user = this.userMapper.selectOne(
                User.builder()
                        .wxId(openid)
                        .build()
        );
        if (user == null) {
            User userToSave = User.builder()
                    .wxId(openid)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .roles("user")
                    .build();

            this.userMapper.insertSelective(userToSave);
            return userToSave;
        }
        return user;
    }

    /**
     * 是否签到
     * @param userId
     * @return
     */
    public BonusEventLog getUserIfSign(Integer userId) {
        return this.bonusEventLogMapper.selectByUserIdIfSign(userId);
    }
}
