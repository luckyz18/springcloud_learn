package com.itmuch.usercenter.service.user;

import com.itmuch.usercenter.dao.user.BonusEventLogMapper;
import com.itmuch.usercenter.domain.dto.user.BonusEventLogDto;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class BonusEventLogService {

    @Autowired
    private BonusEventLogMapper bonusEventLogMapper;

    /**
     * 查看积分明细
     *
     * @return
     */
    public List<BonusEventLogDto> findAllBonusEventLog(){
        List<BonusEventLog> bonusEventLogs = this.bonusEventLogMapper.selectBonusEventLog();
        List<BonusEventLogDto> res = new LinkedList<>();
        for (BonusEventLog log : bonusEventLogs) {
            Date createTime = log.getCreateTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(createTime);
            BonusEventLogDto bonusEventLogDto = new BonusEventLogDto();
            BeanUtils.copyProperties(log,bonusEventLogDto);
            bonusEventLogDto.setCreateTime(format);
            res.add(bonusEventLogDto);
        }
        return res;
    }
}
