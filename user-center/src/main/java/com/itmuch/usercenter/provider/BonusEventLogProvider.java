package com.itmuch.usercenter.provider;

import com.itmuch.usercenter.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Slf4j
public class BonusEventLogProvider {

    public String selectByUserIdIfSign(@Param("userId") Integer userId){
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from bonus_event_log where user_id = #{userId} ");
        String startTime = DateTimeUtil.getStartTime();
        String endTime = DateTimeUtil.getEndTime();
        sql.append(" and create_time between '" + startTime +"' and '" + endTime +"'");
        sql.append(" limit 1 ");
        log.info("|||sql" + sql.toString());
        return sql.toString();
    }
}
