package com.itmuch.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRespDTO {
    /**
     * token
     */
    private JwtTokenRespDTO token;
    /**
     * 响应 用户信息
     */
    private UserRespDTO user;
}
