package com.itmuch.usercenter.dao.user;

import com.itmuch.usercenter.domain.dto.user.BonusEventLogDto;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.provider.BonusEventLogProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BonusEventLogMapper extends Mapper<BonusEventLog> {
    List<BonusEventLog> selectBonusEventLog();

    @SelectProvider(type = BonusEventLogProvider.class,method = "selectByUserIdIfSign")
    BonusEventLog selectByUserIdIfSign(@Param("userId") Integer userId);
}