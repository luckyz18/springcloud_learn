package com.itmuch.contentcenter.service.content;

import com.itmuch.contentcenter.dao.content.NoticeMapper;
import com.itmuch.contentcenter.domain.entity.content.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    public Notice getNewest() {
        Notice notice = this.noticeMapper.selectNewest();
        return notice;
    }
}
