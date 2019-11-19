package com.itmuch.contentcenter.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;

@Slf4j
public class ShareProvider {

    public String selectByShareIds(@Param("shareString") String shareString){
        StringBuffer sb = new StringBuffer();
        sb.append("select * from share where 1=1 ");
        if (shareString != null && !"".equals(shareString)){
            sb.append(" and id in " + shareString);
        }
        log.info("||||sql " + sb.toString());
        return sb.toString();
    }

}
