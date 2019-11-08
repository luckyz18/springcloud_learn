package com.itmuch.contentcenter.controller.content;

import com.itmuch.contentcenter.auth.CheckAuthorization;
import com.itmuch.contentcenter.domain.dto.content.ShareAuditDto;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.service.content.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * 管理员审核
 */
@RestController
@RequestMapping("/admin/shares")
public class ShareAuditController {
    @Autowired
    private ShareService shareService;

    @PostMapping("/audit/{id}")
    @CheckAuthorization("admin")
    public Share auditById(@PathVariable("id") Integer id,
                           @RequestBody ShareAuditDto auditDto){
        return this.shareService.auditById(id,auditDto);
    }


}
