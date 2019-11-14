package com.itmuch.contentcenter.dao.content;

import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.provider.ShareProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ShareMapper extends Mapper<Share> {

    // 这种方法也可
    //@SelectProvider(type = ShareProvider.class,method = "selecByParam")

    List<Share> selecByParam(@Param("title") String title);
    
    
    
}