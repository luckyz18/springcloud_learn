package com.itmuch.contentcenter.controller.content;

import com.itmuch.contentcenter.domain.entity.content.Notice;
import com.itmuch.contentcenter.service.content.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/newest")
    public Notice getNewest(){
        return this.noticeService.getNewest();
    }
}
