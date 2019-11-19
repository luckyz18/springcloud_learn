package com.itmuch.contentcenter.dao.content;

import com.itmuch.contentcenter.domain.entity.content.Notice;
import tk.mybatis.mapper.common.Mapper;

public interface NoticeMapper extends Mapper<Notice> {
    Notice selectNewest();
}