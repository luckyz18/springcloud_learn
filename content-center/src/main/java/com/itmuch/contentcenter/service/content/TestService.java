package com.itmuch.contentcenter.service.content;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {
    @SentinelResource(value = "common")
    public String common(){
        log.info("common....");
        return "common";
    }
}
