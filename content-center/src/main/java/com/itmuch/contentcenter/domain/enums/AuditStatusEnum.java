package com.itmuch.contentcenter.domain.enums;

import lombok.Getter;

@Getter
public enum AuditStatusEnum {
    /**
     * 待审核
     */
    NOT_YET,
    /**
     * 审核已通过
     */
    PASS,
    /**
     * 审核不通过
     */
    REJECT

}
