package com.itmuch.contentcenter.domain.dto.content;

import com.itmuch.contentcenter.domain.enums.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareAuditDto {
    //审核状态
    private AuditStatusEnum auditStatusEnum;
    //原因
    String reson;
}
