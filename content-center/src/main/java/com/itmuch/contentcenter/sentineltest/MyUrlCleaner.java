package com.itmuch.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class MyUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        String[] split = originUrl.split("/");
        //将数字转换为特定的占位符
        /*Arrays.stream(split)
                .map(s -> {
                    if (NumberUtils.isNumber(s)){
                        return "{number}";
                    }
                    return s;
                })
                .reduce((a,b) -> a+"/"+b)
                .orElse("");
       */
        return Arrays.stream(split)
                .map(s -> NumberUtils.isNumber(s) ? "{number}" : s)
                .reduce((a, b) -> a + "/" + b)
                .orElse("");
    }
}
