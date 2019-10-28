package com.itmuch.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestControllerBlockHandler {

    public static String blockFun(String a, BlockException e){
        // 如果被保护的资源被限流或者降级了，就会抛出BlockException
        log.warn(" 限流或者 降级  block",e);
        return "限流或者 降级  block";
    }
}
