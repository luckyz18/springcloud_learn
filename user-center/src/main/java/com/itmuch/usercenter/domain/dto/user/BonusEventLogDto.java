package com.itmuch.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusEventLogDto {
    /**
     * 时间
     */
    private String createTime;
    /**
     *详细描述
     */
    private String description;
    /**
     *事件
     */
    private String event;
    /**
     *
     */
    private Integer id;
    /**
     *用户ID
     */
    private String userId;
    /**
     *积分值
     */
    private Integer value;


}
