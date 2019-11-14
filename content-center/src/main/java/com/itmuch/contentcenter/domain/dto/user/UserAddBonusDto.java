package com.itmuch.contentcenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusDto {

    /**
     * 为谁添加积分
     */
    private Integer uid;
    /**
     * 添加的积分数
     */
    private Integer bonus;
}
