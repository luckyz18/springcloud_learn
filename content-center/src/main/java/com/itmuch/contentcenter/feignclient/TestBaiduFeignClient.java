package com.itmuch.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//name 必须写 否则服务起不来，name随便写
@FeignClient(name = "baidu",url = "www.baidu.com")
public interface TestBaiduFeignClient {
    @GetMapping("")
    public String index();
}
