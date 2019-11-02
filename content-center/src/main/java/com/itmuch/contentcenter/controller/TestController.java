package com.itmuch.contentcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.itmuch.contentcenter.domain.dto.user.UserDto;
import com.itmuch.contentcenter.feignclient.TestBaiduFeignClient;
import com.itmuch.contentcenter.rocketmq.MySource;
import com.itmuch.contentcenter.sentineltest.TestControllerBlockHandler;
import com.itmuch.contentcenter.service.content.TestService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))  //+final
public class TestController {
    /* @Resource
     private ShareMapper shareMapper;
 */
    //@GetMapping("/test2")
    /*public List<Share> testInsert() {
        Share share = new Share();
        share.setAuditStatus("1");
        share.setAuthor("aa");
        share.setBuyCount(1);
        share.setCover("kk");
        share.setTitle("title");
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setUserId(1);

        shareMapper.insertSelective(share);
        List<Share> shares = shareMapper.selectAll();
        return shares;


    }*/
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test2")
    public List<ServiceInstance> getInstance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }

    @Autowired
    TestBaiduFeignClient baiduFeignClient;

    @GetMapping("/baidutest")
    public String testBaidu() {
        return baiduFeignClient.index();
    }

    @Autowired
    private TestService testService;

    @GetMapping("/test-a")
    public String testA() {
        this.testService.common();
        return "test-a";
    }

    @GetMapping("/test-b")
    public String testB() {
        this.testService.common();
        return "test-b";
    }

    @GetMapping("/test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a,
                          @RequestParam(required = false) String b) {

        return a+  " " + b;
    }

    // sentinel api  这种方式很不优雅 之后用注解的方式来解决
    @GetMapping("/test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        // 这里不使用try-with-resources是因为Tracer.trace会统计不上异常
        Entry entry = null;
        try {
            // 定义一个sentinel保护的资源，名称为test-sentinel-api
            entry = SphU.entry(resourceName);
            // 标识对test-sentinel-api调用来源为test-origin（用于流控规则中“针对来源”的配置）
            ContextUtil.enter(resourceName, "test-origin");
            // 模拟执行被保护的业务逻辑耗时
            Thread.sleep(100);
            return a;
        } catch (BlockException e) {
            // 如果被保护的资源被限流或者降级了，就会抛出BlockException
            log.warn("资源被限流或降级了", e);
            return "资源被限流或降级了";
        } catch (InterruptedException e) {
            // 对业务异常进行统计
            Tracer.trace(e);
            return "发生InterruptedException";
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-wfw")
    @SentinelResource(value = "test-sentinel-resource",
                        blockHandler = "blockFun",
                        blockHandlerClass = TestControllerBlockHandler.class,
                        fallback = "fallbackFun")
    public String testSentinelResource(@RequestParam(required = false) String a) {
        if (StringUtils.isBlank(a)){
            throw new IllegalArgumentException("a not be blank");
        }
        return a;
    }
    /**
     * 处理限流 或降级
     * 处理BlockException的函数（处理限流）
     * 太臃肿了 我们可以把这个block 类独立出去
     */
    /*public String blockFun(String a,BlockException e){
        // 如果被保护的资源被限流或者降级了，就会抛出BlockException
        log.warn(" 限流或者 降级  block",e);
        return "限流或者 降级  block";
    }*/
    /**
     * 处理降级 1.6之前
     * 一旦升级成 1.6 ，可以处理 throwable 所有的异常，开始可以针对所有类型的异常进行处理
     * 目前版本 1.5.2不支持把 fallback 独立出去
     * 1.6 可以
     */
    public String fallbackFun(String a){
        return "降级  fallback";
    }

    // resttemplate 整合 sentinel
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test-restTemplate-sentinel/{userId}")
    public UserDto test(@PathVariable("userId") Integer userId) {
        // 调用user-center服务的接口（此时user-center即为服务提供者）
        return restTemplate.getForObject(
                "http://user-center/users/{userId}", UserDto.class, userId);
    }

    //stream生产消息
    @Autowired
    private  Source source;
    @GetMapping("test-stream")
    public String testStream(){
        this.source.output().send(
                MessageBuilder.withPayload(
                        "我是消息体"
                ).build()
        );
        return "success";
    }

    @Autowired
    private MySource mySource;
    @GetMapping("test-stream2")
    public String testStream2(){
        this.mySource.output().send(
                MessageBuilder.withPayload(
                        "消息体aaa"
                ).build()
        );
        return "success";
    }




    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        //for (int i = 0; i <100 ; i++) {
        //    String s = restTemplate.getForObject("http://localhost:8010/actuator/sentinel",String.class);
        //    Thread.sleep(500);
        //}

        /*for (int i = 0; i <100 ; i++) {
            String forObject = restTemplate.getForObject("http://localhost:8010/test-a", String.class);
            System.out.println(forObject);
        }*/

    }


}
