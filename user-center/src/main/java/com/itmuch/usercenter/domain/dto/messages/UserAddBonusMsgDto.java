package com.itmuch.usercenter.domain.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusMsgDto {
    /**
     * 为谁添加积分
     */
    private Integer uid;
    /**
     * 添加的积分数
     */
    private Integer bonus;

}
